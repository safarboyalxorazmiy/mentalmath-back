package uz.mentalmath.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class UserDto {
    
    // Registration Request
    public record RegisterRequest(
        String firstName,
        String lastName,
        LocalDate birthDate,
        String avatar,
        String password
    ) {}
    
    // Login Request
    public record LoginRequest(
        String uniqueId,
        String password
    ) {}
    
    // User Response
    public record UserResponse(
        Long id,
        String uniqueId,
        String firstName,
        String lastName,
        LocalDate birthDate,
        int age,
        String avatar,
        Integer currentLevel,
        Long totalXp,
        Integer totalProblems,
        Integer correctAnswers,
        Integer wrongAnswers,
        Integer longestStreak,
        Integer currentStreak,
        Long totalPracticeTimeSeconds,
        LocalDateTime lastPracticeAt,
        LocalDateTime createdAt
    ) {}
    
    // Search Request
    public record SearchRequest(
        String query,
        String uniqueId
    ) {}
    
    // Stats Response
    public record StatsResponse(
        String firstName,
        String lastName,
        int age,
        String avatar,
        Integer currentLevel,
        Long totalXp,
        Integer totalProblems,
        Integer correctAnswers,
        Integer wrongAnswers,
        double accuracy,
        Integer longestStreak,
        String totalPracticeTime,
        LocalDateTime lastPracticeAt
    ) {}

    public record LeaderboardItem(
            int rank,
            String uniqueId,
            String firstName,
            String lastName,
            String avatar,
            int age,
            Integer currentLevel,
            Long totalXp,
            Integer totalProblems,
            double accuracy,
            Integer longestStreak,
            Integer correctAnswers,
            Integer wrongAnswers,
            String totalPracticeTime,
            LocalDateTime lastPracticeAt
    ) {}
}
