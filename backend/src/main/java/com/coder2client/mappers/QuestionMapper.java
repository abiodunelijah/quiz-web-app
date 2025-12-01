package com.coder2client.mappers;

import com.coder2client.dtos.QuestionDto;
import com.coder2client.dtos.QuestionRequest;
import com.coder2client.entities.Option;
import com.coder2client.entities.Question;
import com.coder2client.entities.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuestionMapper {
    private final OptionMapper optionMapper;

    public QuestionDto toDTO(Question question, boolean includeAnswers) {
        if (question == null) return null;

        return QuestionDto.builder()
                .id(question.getId())
                .text(question.getText())
                .type(question.getType().name())
                .options(question.getOptions() != null ?
                        question.getOptions().stream()
                                .map(o -> optionMapper.toDTO(o, includeAnswers))
                                .collect(Collectors.toList()) :
                        Collections.emptyList())
                .build();
    }

    public Question toEntity(QuestionRequest request, Quiz quiz) {
        if (request == null) return null;

        Question question = Question.builder()
                .text(request.getText())
                .type(Question.QuestionType.valueOf(request.getType()))
                .quiz(quiz)
                .build();

        if (request.getOptions() != null) {
            List<Option> options = request.getOptions().stream()
                    .map(or -> optionMapper.toEntity(or, question))
                    .collect(Collectors.toList());
            question.setOptions(options);
        }

        return question;
    }
}