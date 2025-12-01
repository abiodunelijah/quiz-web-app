package com.coder2client.services;

import com.coder2client.dtos.AnswerRequest;
import com.coder2client.dtos.AttemptDto;
import com.coder2client.dtos.SubmitRequest;
import com.coder2client.entities.*;
import com.coder2client.mappers.AttemptMapper;
import com.coder2client.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttemptServiceImpl implements AttemptService {

    private final AttemptRepository attemptRepository;
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final AttemptMapper attemptMapper;

    @Transactional
    public AttemptDto startAttempt(Long quizId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        Attempt attempt = Attempt.builder()
                .user(user)
                .quiz(quiz)
                .score(0.0)
                .build();

        Attempt saved = attemptRepository.save(attempt);
        return attemptMapper.toDTO(saved);
    }

    @Transactional
    public AttemptDto submitAttempt(Long attemptId, SubmitRequest request) {
        Attempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Attempt not found"));

        if (attempt.getSubmittedAt() != null) {
            throw new RuntimeException("Attempt already submitted");
        }

        int correctAnswers = 0;
        int totalQuestions = attempt.getQuiz().getQuestions().size();

        for (AnswerRequest answer : request.getAnswers()) {
            Question question = questionRepository.findById(answer.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));
            Option selectedOption = optionRepository.findById(answer.getSelectedOptionId())
                    .orElseThrow(() -> new RuntimeException("Option not found"));

            UserAnswer userAnswer = UserAnswer.builder()
                    .attempt(attempt)
                    .question(question)
                    .selectedOption(selectedOption)
                    .build();

            attempt.getUserAnswers().add(userAnswer);

            if (selectedOption.getIsCorrect()) {
                correctAnswers++;
            }
        }

        double score = totalQuestions > 0 ?
                (double) correctAnswers / totalQuestions * 100 : 0;
        attempt.setScore(score);
        attempt.setSubmittedAt(LocalDateTime.now());

        Attempt saved = attemptRepository.save(attempt);
        return attemptMapper.toDTO(saved);
    }

    public AttemptDto getAttemptResult(Long attemptId) {
        Attempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Attempt not found"));
        return attemptMapper.toDTO(attempt);
    }

    public List<AttemptDto> getUserAttempts(Long userId) {
        return attemptRepository.findByUserId(userId).stream()
                .map(attemptMapper::toDTO)
                .collect(Collectors.toList());
    }

}
