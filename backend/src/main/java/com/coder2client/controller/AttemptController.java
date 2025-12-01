package com.coder2client.controller;

import com.coder2client.dtos.AttemptDto;
import com.coder2client.dtos.SubmitRequest;
import com.coder2client.services.AttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AttemptController {
    private final AttemptService attemptService;

    @PostMapping("/quizzes/{quizId}/attempt")
    public ResponseEntity<AttemptDto> startAttempt(
            @PathVariable Long quizId,
            @RequestHeader("User-Id") Long userId) {
        AttemptDto attempt = attemptService.startAttempt(quizId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(attempt);
    }

    @PostMapping("/attempts/{attemptId}/submit")
    public ResponseEntity<AttemptDto> submitAttempt(
            @PathVariable Long attemptId,
            @RequestBody SubmitRequest request) {
        AttemptDto result = attemptService.submitAttempt(attemptId, request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/attempts/{attemptId}/result")
    public ResponseEntity<AttemptDto> getAttemptResult(@PathVariable Long attemptId) {
        AttemptDto result = attemptService.getAttemptResult(attemptId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/users/{userId}/attempts")
    public ResponseEntity<List<AttemptDto>> getUserAttempts(@PathVariable Long userId) {
        List<AttemptDto> attempts = attemptService.getUserAttempts(userId);
        return ResponseEntity.ok(attempts);
    }
}