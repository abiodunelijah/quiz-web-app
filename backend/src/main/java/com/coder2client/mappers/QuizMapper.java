package com.coder2client.mappers;

import com.coder2client.dtos.CreateQuizRequest;
import com.coder2client.dtos.QuizDto;
import com.coder2client.entities.Question;
import com.coder2client.entities.Quiz;
import com.coder2client.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuizMapper {
    private final UserMapper userMapper;
    private final QuestionMapper questionMapper;

    public QuizDto toDTO(Quiz quiz, boolean includeAnswers) {
        if (quiz == null) return null;

        return QuizDto.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .timeLimit(quiz.getTimeLimit())
                .createdBy(userMapper.toDTO(quiz.getCreatedBy()))
                .createdAt(quiz.getCreatedAt())
                .questions(quiz.getQuestions() != null ?
                        quiz.getQuestions().stream()
                                .map(q -> questionMapper.toDTO(q, includeAnswers))
                                .collect(Collectors.toList()) :
                        Collections.emptyList())
                .build();
    }

    public Quiz toEntity(CreateQuizRequest request, User creator) {
        if (request == null) return null;

        Quiz quiz = Quiz.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .timeLimit(request.getTimeLimit())
                .createdBy(creator)
                .build();

        if (request.getQuestions() != null) {
            List<Question> questions = request.getQuestions().stream()
                    .map(qr -> questionMapper.toEntity(qr, quiz))
                    .collect(Collectors.toList());
            quiz.setQuestions(questions);
        }

        return quiz;
    }
}