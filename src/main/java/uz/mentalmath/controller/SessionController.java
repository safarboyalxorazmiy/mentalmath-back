package uz.mentalmath.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.mentalmath.dto.SessionDto.*;
import uz.mentalmath.service.SessionService;

import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*")
public class SessionController {
    
    private final SessionService sessionService;
    
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }
    
    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startSession(@RequestBody StartSessionRequest request) {
        SessionResponse session = sessionService.startSession(request);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", session
        ));
    }
    
    @PostMapping("/answer")
    public ResponseEntity<Map<String, Object>> submitAnswer(@RequestBody SubmitAnswerRequest request) {
        AnswerResult result = sessionService.submitAnswer(request);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", result
        ));
    }
    
    @GetMapping("/{sessionId}/next")
    public ResponseEntity<Map<String, Object>> getNextProblem(@PathVariable Long sessionId) {
        ProblemResponse problem = sessionService.getNextProblem(sessionId);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", problem
        ));
    }
}
