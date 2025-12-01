package com.coder2client.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttemptDto {
    private Long id;
    private Long userId;
    private Long quizId;
    private String quizTitle;
    private Double score;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
}
