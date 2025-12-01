package com.coder2client.services;

import com.coder2client.dtos.CreateQuizRequest;
import com.coder2client.dtos.QuizDto;

import java.util.List;

public interface QuizService {
    QuizDto createQuiz(CreateQuizRequest request, Long userId);
    List<QuizDto> getAllQuizzes();
    QuizDto getQuizById(Long id, boolean includeAnswers);
}
