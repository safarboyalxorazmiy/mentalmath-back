package uz.mentalmath.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "problems")
public class Problem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;
    
    @Column(nullable = false)
    private String expression;
    
    @Column(nullable = false)
    private Integer correctAnswer;
    
    private Integer userAnswer;
    
    @Column(nullable = false)
    private Boolean isCorrect = false;
    
    @Column(nullable = false)
    private Integer problemOrder;
    
    private Long responseTimeMs;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public Problem() {}
    
    public Problem(Session session, String expression, Integer correctAnswer, Integer problemOrder) {
        this.session = session;
        this.expression = expression;
        this.correctAnswer = correctAnswer;
        this.problemOrder = problemOrder;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }
    
    public String getExpression() { return expression; }
    public void setExpression(String expression) { this.expression = expression; }
    
    public Integer getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(Integer correctAnswer) { this.correctAnswer = correctAnswer; }
    
    public Integer getUserAnswer() { return userAnswer; }
    public void setUserAnswer(Integer userAnswer) { this.userAnswer = userAnswer; }
    
    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
    
    public Integer getProblemOrder() { return problemOrder; }
    public void setProblemOrder(Integer problemOrder) { this.problemOrder = problemOrder; }
    
    public Long getResponseTimeMs() { return responseTimeMs; }
    public void setResponseTimeMs(Long responseTimeMs) { this.responseTimeMs = responseTimeMs; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
