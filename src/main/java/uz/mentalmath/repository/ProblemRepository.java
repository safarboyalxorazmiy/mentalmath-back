package uz.mentalmath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.mentalmath.entity.Problem;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<Problem> findBySessionIdOrderByProblemOrder(Long sessionId);

    @Query("SELECT p FROM Problem p WHERE p.session.id = :sessionId AND p.userAnswer IS NULL ORDER BY p.problemOrder ASC LIMIT 1")
    Optional<Problem> findNextUnansweredProblem(@Param("sessionId") Long sessionId);

    @Query("SELECT COUNT(p) FROM Problem p WHERE p.session.id = :sessionId AND p.userAnswer IS NOT NULL")
    Integer countAnsweredProblems(@Param("sessionId") Long sessionId);
}