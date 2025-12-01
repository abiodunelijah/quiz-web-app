package com.coder2client.mappers;

import com.coder2client.dtos.OptionDto;
import com.coder2client.dtos.OptionRequest;
import com.coder2client.entities.Option;
import com.coder2client.entities.Question;
import org.springframework.stereotype.Component;

@Component
public class OptionMapper {
    public OptionDto toDTO(Option option, boolean includeAnswer) {
        if (option == null) return null;

        return OptionDto.builder()
                .id(option.getId())
                .text(option.getText())
                .isCorrect(includeAnswer ? option.getIsCorrect() : null)
                .build();
    }

    public Option toEntity(OptionRequest request, Question question) {
        if (request == null) return null;

        return Option.builder()
                .text(request.getText())
                .isCorrect(request.getIsCorrect() != null ? request.getIsCorrect() : false)
                .question(question)
                .build();
    }
}
