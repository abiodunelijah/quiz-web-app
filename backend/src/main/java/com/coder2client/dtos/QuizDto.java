package com.coder2client.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizDto {
    private Long id;
    private String title;
    private String description;
    private Integer timeLimit;
    private UserDto createdBy;
    private LocalDateTime createdAt;
    private List<QuestionDto> questions;
}
