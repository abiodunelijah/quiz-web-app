package com.coder2client.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionDto {
    private Long id;
    private String text;
    private Boolean isCorrect; // Only sent to quiz creator
}
