package uz.mentalmath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.mentalmath.entity.Session;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    
    List<Session> findByUserIdOrderByStartedAtDesc(Long userId);
    
    @Query("SELECT s FROM Session s WHERE s.user.id = :userId AND s.completed = false ORDER BY s.startedAt DESC")
    Optional<Session> findActiveSession(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(s) FROM Session s WHERE s.user.id = :userId AND s.completed = true")
    Long countCompletedSessions(@Param("userId") Long userId);
}
