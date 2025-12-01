package com.coder2client.services;

import com.coder2client.dtos.CreateQuizRequest;
import com.coder2client.dtos.QuizDto;
import com.coder2client.entities.Quiz;
import com.coder2client.entities.User;
import com.coder2client.mappers.QuizMapper;
import com.coder2client.repositories.QuizRepository;
import com.coder2client.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuizMapper quizMapper;

    @Transactional
    public QuizDto createQuiz(CreateQuizRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Quiz quiz = quizMapper.toEntity(request, user);
        Quiz saved = quizRepository.save(quiz);

        return quizMapper.toDTO(saved, true);
    }

    public List<QuizDto> getAllQuizzes() {
        return quizRepository.findAll().stream()
                .map(q -> quizMapper.toDTO(q, false))
                .collect(Collectors.toList());
    }

    public QuizDto getQuizById(Long id, boolean includeAnswers) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        return quizMapper.toDTO(quiz, includeAnswers);
    }


}
