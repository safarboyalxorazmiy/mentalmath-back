package uz.mentalmath.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.mentalmath.dto.UserDto.*;
import uz.mentalmath.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        UserResponse user = userService.register(request);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", user,
            "message", "Ro'yxatdan o'tish muvaffaqiyatli"
        ));
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        UserResponse user = userService.login(request);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", user
        ));
    }
    
    @GetMapping("/{uniqueId}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable String uniqueId) {
        UserResponse user = userService.getByUniqueId(uniqueId);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", user
        ));
    }
    
    @PostMapping("/search")
    public ResponseEntity<Map<String, Object>> search(@RequestBody SearchRequest request) {
        List<StatsResponse> results = userService.search(request);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", results,
            "count", results.size()
        ));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<Map<String, Object>> getLeaderboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        var leaderboard = userService.getLeaderboard(page, size);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", leaderboard.getContent(),
                "totalPages", leaderboard.getTotalPages(),
                "totalElements", leaderboard.getTotalElements(),
                "currentPage", page
        ));
    }
    
    @GetMapping("/{uniqueId}/stats")
    public ResponseEntity<Map<String, Object>> getStats(@PathVariable String uniqueId) {
        StatsResponse stats = userService.getStats(uniqueId);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", stats
        ));
    }
}
