package uz.mentalmath.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 8)
    private String uniqueId;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false)
    private LocalDate birthDate;
    
    @Column(nullable = false)
    private String avatar;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private Integer currentLevel = 1;
    
    @Column(nullable = false)
    private Long totalXp = 0L;
    
    @Column(nullable = false)
    private Integer totalProblems = 0;
    
    @Column(nullable = false)
    private Integer correctAnswers = 0;
    
    @Column(nullable = false)
    private Integer wrongAnswers = 0;
    
    @Column(nullable = false)
    private Integer longestStreak = 0;
    
    @Column(nullable = false)
    private Integer currentStreak = 0;
    
    @Column(nullable = false)
    private Long totalPracticeTimeSeconds = 0L;
    
    private LocalDateTime lastPracticeAt;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions = new ArrayList<>();
    
    public User() {}
    
    public User(String uniqueId, String firstName, String lastName, LocalDate birthDate, String avatar, String password) {
        this.uniqueId = uniqueId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.avatar = avatar;
        this.password = password;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUniqueId() { return uniqueId; }
    public void setUniqueId(String uniqueId) { this.uniqueId = uniqueId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public Integer getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(Integer currentLevel) { this.currentLevel = currentLevel; }
    
    public Long getTotalXp() { return totalXp; }
    public void setTotalXp(Long totalXp) { this.totalXp = totalXp; }
    
    public Integer getTotalProblems() { return totalProblems; }
    public void setTotalProblems(Integer totalProblems) { this.totalProblems = totalProblems; }
    
    public Integer getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(Integer correctAnswers) { this.correctAnswers = correctAnswers; }
    
    public Integer getWrongAnswers() { return wrongAnswers; }
    public void setWrongAnswers(Integer wrongAnswers) { this.wrongAnswers = wrongAnswers; }
    
    public Integer getLongestStreak() { return longestStreak; }
    public void setLongestStreak(Integer longestStreak) { this.longestStreak = longestStreak; }
    
    public Integer getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(Integer currentStreak) { this.currentStreak = currentStreak; }
    
    public Long getTotalPracticeTimeSeconds() { return totalPracticeTimeSeconds; }
    public void setTotalPracticeTimeSeconds(Long totalPracticeTimeSeconds) { this.totalPracticeTimeSeconds = totalPracticeTimeSeconds; }
    
    public LocalDateTime getLastPracticeAt() { return lastPracticeAt; }
    public void setLastPracticeAt(LocalDateTime lastPracticeAt) { this.lastPracticeAt = lastPracticeAt; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public List<Session> getSessions() { return sessions; }
    public void setSessions(List<Session> sessions) { this.sessions = sessions; }
    
    public int getAge() {
        return LocalDate.now().getYear() - birthDate.getYear();
    }
}
