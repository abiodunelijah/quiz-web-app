package com.coder2client.services;

import com.coder2client.dtos.AnswerRequest;
import com.coder2client.dtos.AttemptDto;
import com.coder2client.dtos.SubmitRequest;
import com.coder2client.entities.*;
import com.coder2client.exceptions.BadRequestException;
import com.coder2client.exceptions.ResourceNotFoundException;
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
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + quizId));

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
                .orElseThrow(() -> new ResourceNotFoundException("Attempt not found with id: " + attemptId));

        if (attempt.getSubmittedAt() != null) {
            throw new BadRequestException("Attempt has already been submitted");
        }

        // Load quiz with questions eagerly
        Quiz quiz = quizRepository.findById(attempt.getQuiz().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        int correctAnswers = 0;
        int totalQuestions = quiz.getQuestions().size();

        for (AnswerRequest answer : request.getAnswers()) {
            Question question = questionRepository.findById(answer.getQuestionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + answer.getQuestionId()));

            Option selectedOption = optionRepository.findById(answer.getSelectedOptionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Option not found with id: " + answer.getSelectedOptionId()));

            // Validate that the option belongs to the question
            if (!selectedOption.getQuestion().getId().equals(question.getId())) {
                throw new BadRequestException("Option " + selectedOption.getId() + " does not belong to question " + question.getId());
            }

            // Validate that the question belongs to the quiz
            if (!question.getQuiz().getId().equals(quiz.getId())) {
                throw new BadRequestException("Question " + question.getId() + " does not belong to this quiz");
            }

            UserAnswer userAnswer = UserAnswer.builder()
                    .attempt(attempt)
                    .question(question)
                    .selectedOption(selectedOption)
                    .build();

            attempt.getUserAnswers().add(userAnswer);

            if (Boolean.TRUE.equals(selectedOption.getIsCorrect())) {
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

    @Transactional
    public AttemptDto getAttemptResult(Long attemptId) {
        Attempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Attempt not found with id: " + attemptId));
        return attemptMapper.toDTO(attempt);
    }

    @Transactional
    public List<AttemptDto> getUserAttempts(Long userId) {
        return attemptRepository.findByUserId(userId).stream()
                .map(attemptMapper::toDTO)
                .collect(Collectors.toList());
    }
}