package com.coder2client.mappers;

import com.coder2client.dtos.AttemptDto;
import com.coder2client.entities.Attempt;
import org.springframework.stereotype.Component;

@Component
public class AttemptMapper {
    public AttemptDto toDTO(Attempt attempt) {
        if (attempt == null) return null;

        return AttemptDto.builder()
                .id(attempt.getId())
                .userId(attempt.getUser().getId())
                .quizId(attempt.getQuiz().getId())
                .quizTitle(attempt.getQuiz().getTitle())
                .score(attempt.getScore())
                .startedAt(attempt.getStartedAt())
                .submittedAt(attempt.getSubmittedAt())
                .build();
    }
}