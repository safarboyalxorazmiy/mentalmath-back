package uz.mentalmath.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessions")
public class Session {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SessionType sessionType;
    
    @Column(nullable = false)
    private Integer level;
    
    @Column(nullable = false)
    private Integer totalProblems = 10;
    
    @Column(nullable = false)
    private Integer correctAnswers = 0;
    
    @Column(nullable = false)
    private Integer wrongAnswers = 0;
    
    @Column(nullable = false)
    private Integer maxStreak = 0;
    
    @Column(nullable = false)
    private Long durationSeconds = 0L;
    
    @Column(nullable = false)
    private Integer xpEarned = 0;
    
    @Column(nullable = false)
    private Boolean completed = false;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime startedAt = LocalDateTime.now();
    
    private LocalDateTime completedAt;
    
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Problem> problems = new ArrayList<>();
    
    public enum SessionType {
        SIMPLE,    // Ko'p hadli oddiy misol
        SEQUENTIAL // Ketma-ket amal (mental)
    }
    
    public Session() {}
    
    public Session(User user, SessionType sessionType, Integer level) {
        this.user = user;
        this.sessionType = sessionType;
        this.level = level;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public SessionType getSessionType() { return sessionType; }
    public void setSessionType(SessionType sessionType) { this.sessionType = sessionType; }
    
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    
    public Integer getTotalProblems() { return totalProblems; }
    public void setTotalProblems(Integer totalProblems) { this.totalProblems = totalProblems; }
    
    public Integer getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(Integer correctAnswers) { this.correctAnswers = correctAnswers; }
    
    public Integer getWrongAnswers() { return wrongAnswers; }
    public void setWrongAnswers(Integer wrongAnswers) { this.wrongAnswers = wrongAnswers; }
    
    public Integer getMaxStreak() { return maxStreak; }
    public void setMaxStreak(Integer maxStreak) { this.maxStreak = maxStreak; }
    
    public Long getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Long durationSeconds) { this.durationSeconds = durationSeconds; }
    
    public Integer getXpEarned() { return xpEarned; }
    public void setXpEarned(Integer xpEarned) { this.xpEarned = xpEarned; }
    
    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }
    
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    
    public List<Problem> getProblems() { return problems; }
    public void setProblems(List<Problem> problems) { this.problems = problems; }
    
    public double getAccuracy() {
        if (totalProblems == 0) return 0;
        return (double) correctAnswers / totalProblems * 100;
    }
}
