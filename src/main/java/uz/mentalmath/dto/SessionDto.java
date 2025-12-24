package uz.mentalmath.dto;

import uz.mentalmath.entity.Session.SessionType;
import java.time.LocalDateTime;
import java.util.List;

public class SessionDto {
    
    // Start Session Request
    public record StartSessionRequest(
        String uniqueId,
        SessionType sessionType,
        String speed // "slow", "medium", "fast" - faqat SEQUENTIAL uchun
    ) {}
    
    // Problem Response
    public record ProblemResponse(
        Long problemId,
        String expression,
        List<String> sequentialOperations, // SEQUENTIAL uchun
        int problemNumber,
        int totalProblems
    ) {}
    
    // Submit Answer Request
    public record SubmitAnswerRequest(
        Long sessionId,
        Long problemId,
        Integer userAnswer,
        Long responseTimeMs
    ) {}
    
    // Answer Result
    public record AnswerResult(
        boolean isCorrect,
        Integer correctAnswer,
        Integer userAnswer,
        int currentStreak,
        int xpEarned,
        boolean sessionComplete,
        SessionResult sessionResult
    ) {}
    
    // Session Result
    public record SessionResult(
        Long sessionId,
        int correctAnswers,
        int wrongAnswers,
        int totalProblems,
        double accuracy,
        int maxStreak,
        int totalXpEarned,
        long durationSeconds,
        boolean leveledUp,
        Integer newLevel
    ) {}
    
    // Session Response
    public record SessionResponse(
        Long sessionId,
        SessionType sessionType,
        Integer level,
        ProblemResponse currentProblem
    ) {}
}
