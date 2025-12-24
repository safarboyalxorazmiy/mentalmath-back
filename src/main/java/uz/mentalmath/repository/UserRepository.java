package uz.mentalmath.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.mentalmath.entity.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUniqueId(String uniqueId);
    
    boolean existsByUniqueId(String uniqueId);
    
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY u.totalXp DESC, u.currentLevel DESC")
    List<User> searchByName(@Param("query") String query);
    
    @Query("SELECT u FROM User u WHERE u.uniqueId = :uniqueId")
    Optional<User> searchByUniqueId(@Param("uniqueId") String uniqueId);

    @Query("SELECT u FROM User u ORDER BY u.totalXp DESC, u.currentLevel DESC")
    Page<User> findAllByOrderByTotalXpDesc(Pageable pageable);
}
