package com.coder2client.controller;


import com.coder2client.dtos.CreateQuizRequest;
import com.coder2client.dtos.QuizDto;
import com.coder2client.services.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class QuizController {
    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<QuizDto> createQuiz(
            @RequestBody CreateQuizRequest request,
            @RequestHeader("User-Id") Long userId) {
        QuizDto quiz = quizService.createQuiz(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(quiz);
    }

    @GetMapping
    public ResponseEntity<List<QuizDto>> getAllQuizzes() {
        List<QuizDto> quizzes = quizService.getAllQuizzes();
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDto> getQuizById(@PathVariable Long id) {
        QuizDto quiz = quizService.getQuizById(id, false);
        return ResponseEntity.ok(quiz);
    }
}
