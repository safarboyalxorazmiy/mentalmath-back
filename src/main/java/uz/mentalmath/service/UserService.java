package uz.mentalmath.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.mentalmath.dto.UserDto.*;
import uz.mentalmath.entity.User;
import uz.mentalmath.exception.ApiException;
import uz.mentalmath.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Transactional
    public UserResponse register(RegisterRequest request) {
        String uniqueId = generateUniqueId();
        
        User user = new User(
            uniqueId,
            request.firstName(),
            request.lastName(),
            request.birthDate(),
            request.avatar(),
            passwordEncoder.encode(request.password())
        );
        
        user = userRepository.save(user);
        return toUserResponse(user);
    }
    
    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByUniqueId(request.uniqueId())
            .orElseThrow(() -> new ApiException("Foydalanuvchi topilmadi"));

        System.out.println(user.getPassword());
        System.out.println("Matches? " + passwordEncoder.matches(request.password(), user.getPassword()));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ApiException("Parol noto'g'ri");
        }
        
        return toUserResponse(user);
    }
    
    public UserResponse getByUniqueId(String uniqueId) {
        User user = userRepository.findByUniqueId(uniqueId)
            .orElseThrow(() -> new ApiException("Foydalanuvchi topilmadi"));
        return toUserResponse(user);
    }
    
    public List<StatsResponse> search(SearchRequest request) {
        List<User> users;
        
        if (request.uniqueId() != null && !request.uniqueId().isBlank()) {
            users = userRepository.searchByUniqueId(request.uniqueId())
                .map(List::of)
                .orElse(List.of());
        } else if (request.query() != null && !request.query().isBlank()) {
            users = userRepository.searchByName(request.query());
        } else {
            throw new ApiException("Qidiruv so'rovi kiritilmadi");
        }
        
        return users.stream()
            .map(this::toStatsResponse)
            .collect(Collectors.toList());
    }
    
    public StatsResponse getStats(String uniqueId) {
        User user = userRepository.findByUniqueId(uniqueId)
            .orElseThrow(() -> new ApiException("Foydalanuvchi topilmadi"));
        return toStatsResponse(user);
    }
    
    private String generateUniqueId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String uniqueId;
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            uniqueId = sb.toString();
        } while (userRepository.existsByUniqueId(uniqueId));
        return uniqueId;
    }
    
    private UserResponse toUserResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getUniqueId(),
            user.getFirstName(),
            user.getLastName(),
            user.getBirthDate(),
            user.getAge(),
            user.getAvatar(),
            user.getCurrentLevel(),
            user.getTotalXp(),
            user.getTotalProblems(),
            user.getCorrectAnswers(),
            user.getWrongAnswers(),
            user.getLongestStreak(),
            user.getCurrentStreak(),
            user.getTotalPracticeTimeSeconds(),
            user.getLastPracticeAt(),
            user.getCreatedAt()
        );
    }
    
    private StatsResponse toStatsResponse(User user) {
        double accuracy = user.getTotalProblems() > 0 
            ? (double) user.getCorrectAnswers() / user.getTotalProblems() * 100 
            : 0;
        
        return new StatsResponse(
            user.getFirstName(),
            user.getLastName(),
            user.getAge(),
            user.getAvatar(),
            user.getCurrentLevel(),
            user.getTotalXp(),
            user.getTotalProblems(),
            user.getCorrectAnswers(),
            user.getWrongAnswers(),
            Math.round(accuracy * 10.0) / 10.0,
            user.getLongestStreak(),
            formatDuration(user.getTotalPracticeTimeSeconds()),
            user.getLastPracticeAt()
        );
    }
    
    private String formatDuration(Long seconds) {
        if (seconds == null || seconds == 0) return "0 daqiqa";
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        if (hours > 0) {
            return hours + " soat " + minutes + " daqiqa";
        }
        return minutes + " daqiqa";
    }

    public Page<LeaderboardItem> getLeaderboard(int page, int size) {
        Page<User> users = userRepository.findAllByOrderByTotalXpDesc(PageRequest.of(page, size));

        List<LeaderboardItem> items = new ArrayList<>();
        int rank = page * size + 1;

        for (User user : users.getContent()) {
            double accuracy = user.getTotalProblems() > 0
                    ? Math.round((double) user.getCorrectAnswers() / user.getTotalProblems() * 1000.0) / 10.0
                    : 0;

            items.add(new LeaderboardItem(
                    rank++,
                    user.getUniqueId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getAvatar(),
                    user.getAge(),
                    user.getCurrentLevel(),
                    user.getTotalXp(),
                    user.getTotalProblems(),
                    accuracy,
                    user.getLongestStreak()
            ));
        }

        return new PageImpl<>(items, users.getPageable(), users.getTotalElements());
    }

}
