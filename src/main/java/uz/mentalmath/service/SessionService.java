package uz.mentalmath.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.mentalmath.dto.SessionDto.*;
import uz.mentalmath.entity.Problem;
import uz.mentalmath.entity.Session;
import uz.mentalmath.entity.Session.SessionType;
import uz.mentalmath.entity.User;
import uz.mentalmath.exception.ApiException;
import uz.mentalmath.repository.ProblemRepository;
import uz.mentalmath.repository.SessionRepository;
import uz.mentalmath.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SessionService {
    
    private static final int PROBLEMS_PER_SESSION = 10;
    private static final int XP_PER_CORRECT = 10;
    private static final int XP_STREAK_BONUS = 5;
    private static final double LEVEL_UP_THRESHOLD = 0.70;
    
    private final SessionRepository sessionRepository;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final ProblemGenerator problemGenerator;
    
    public SessionService(SessionRepository sessionRepository, ProblemRepository problemRepository,
                         UserRepository userRepository, ProblemGenerator problemGenerator) {
        this.sessionRepository = sessionRepository;
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
        this.problemGenerator = problemGenerator;
    }
    
    @Transactional
    public SessionResponse startSession(StartSessionRequest request) {
        User user = userRepository.findByUniqueId(request.uniqueId())
            .orElseThrow(() -> new ApiException("Foydalanuvchi topilmadi"));
        
        // Avvalgi tugallanmagan sessiyani tekshirish
        sessionRepository.findActiveSession(user.getId())
            .ifPresent(s -> {
                s.setCompleted(true);
                s.setCompletedAt(LocalDateTime.now());
                sessionRepository.save(s);
            });
        
        // Yangi sessiya yaratish
        Session session = new Session(user, request.sessionType(), user.getCurrentLevel());
        session = sessionRepository.save(session);
        
        // Misollarni generatsiya qilish
        for (int i = 0; i < PROBLEMS_PER_SESSION; i++) {
            var generated = problemGenerator.generate(user.getCurrentLevel(), request.sessionType());
            Problem problem = new Problem(session, generated.expression(), generated.answer(), i + 1);
            problemRepository.save(problem);
        }
        
        // Birinchi misolni qaytarish
        Problem firstProblem = problemRepository.findNextUnansweredProblem(session.getId())
            .orElseThrow(() -> new ApiException("Misol topilmadi"));
        
        return new SessionResponse(
            session.getId(),
            session.getSessionType(),
            session.getLevel(),
            toProblemResponse(firstProblem, request.sessionType())
        );
    }
    
    @Transactional
    public AnswerResult submitAnswer(SubmitAnswerRequest request) {
        Problem problem = problemRepository.findById(request.problemId())
            .orElseThrow(() -> new ApiException("Misol topilmadi"));
        
        Session session = problem.getSession();
        User user = session.getUser();
        
        if (session.getCompleted()) {
            throw new ApiException("Sessiya allaqachon tugagan");
        }
        
        // Javobni tekshirish
        boolean isCorrect = problem.getCorrectAnswer().equals(request.userAnswer());
        problem.setUserAnswer(request.userAnswer());
        problem.setIsCorrect(isCorrect);
        problem.setResponseTimeMs(request.responseTimeMs());
        problemRepository.save(problem);
        
        // Statistikani yangilash
        int xpEarned = 0;
        if (isCorrect) {
            session.setCorrectAnswers(session.getCorrectAnswers() + 1);
            user.setCorrectAnswers(user.getCorrectAnswers() + 1);
            user.setCurrentStreak(user.getCurrentStreak() + 1);
            
            // XP hisoblash
            xpEarned = XP_PER_CORRECT;
            if (user.getCurrentStreak() >= 3) {
                xpEarned += XP_STREAK_BONUS;
            }
            user.setTotalXp(user.getTotalXp() + xpEarned);
            session.setXpEarned(session.getXpEarned() + xpEarned);
            
            // Streak yangilash
            if (user.getCurrentStreak() > user.getLongestStreak()) {
                user.setLongestStreak(user.getCurrentStreak());
            }
            if (user.getCurrentStreak() > session.getMaxStreak()) {
                session.setMaxStreak(user.getCurrentStreak());
            }
        } else {
            session.setWrongAnswers(session.getWrongAnswers() + 1);
            user.setWrongAnswers(user.getWrongAnswers() + 1);
            user.setCurrentStreak(0);
        }
        
        user.setTotalProblems(user.getTotalProblems() + 1);
        
        // Sessiya tugadimi tekshirish
        int answeredCount = problemRepository.countAnsweredProblems(session.getId());
        boolean sessionComplete = answeredCount >= PROBLEMS_PER_SESSION;
        
        SessionResult sessionResult = null;
        if (sessionComplete) {
            session.setCompleted(true);
            session.setCompletedAt(LocalDateTime.now());
            session.setDurationSeconds(Duration.between(session.getStartedAt(), session.getCompletedAt()).getSeconds());
            
            user.setTotalPracticeTimeSeconds(user.getTotalPracticeTimeSeconds() + session.getDurationSeconds());
            user.setLastPracticeAt(LocalDateTime.now());
            
            // Level oshish
            boolean leveledUp = false;
            Integer newLevel = null;
            double accuracy = session.getAccuracy();
            if (accuracy >= LEVEL_UP_THRESHOLD * 100 && user.getCurrentLevel() < 10) {
                user.setCurrentLevel(user.getCurrentLevel() + 1);
                leveledUp = true;
                newLevel = user.getCurrentLevel();
            }
            
            sessionResult = new SessionResult(
                session.getId(),
                session.getCorrectAnswers(),
                session.getWrongAnswers(),
                PROBLEMS_PER_SESSION,
                Math.round(accuracy * 10.0) / 10.0,
                session.getMaxStreak(),
                session.getXpEarned(),
                session.getDurationSeconds(),
                leveledUp,
                newLevel
            );
        }
        
        sessionRepository.save(session);
        userRepository.save(user);
        
        return new AnswerResult(
            isCorrect,
            problem.getCorrectAnswer(),
            request.userAnswer(),
            user.getCurrentStreak(),
            xpEarned,
            sessionComplete,
            sessionResult
        );
    }
    
    public ProblemResponse getNextProblem(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new ApiException("Sessiya topilmadi"));
        
        if (session.getCompleted()) {
            throw new ApiException("Sessiya tugagan");
        }
        
        Problem nextProblem = problemRepository.findNextUnansweredProblem(sessionId)
            .orElseThrow(() -> new ApiException("Barcha misollar bajarilgan"));
        
        return toProblemResponse(nextProblem, session.getSessionType());
    }

    private ProblemResponse toProblemResponse(Problem problem, SessionType sessionType) {
        List<String> operations = null;

        if (sessionType == SessionType.SEQUENTIAL) {
            String expr = problem.getExpression().replaceAll("\\s+", "");

            operations = new ArrayList<>();

            // first number
            Matcher first = Pattern.compile("^\\d+").matcher(expr);
            if (first.find()) {
                operations.add(first.group());
            }

            // +number or -number
            Matcher rest = Pattern.compile("[+-]\\d+").matcher(expr);
            while (rest.find()) {
                operations.add(rest.group());
            }
        }

        return new ProblemResponse(
                problem.getId(),
                problem.getExpression(),
                operations,
                problem.getProblemOrder(),
                PROBLEMS_PER_SESSION
        );
    }

}
