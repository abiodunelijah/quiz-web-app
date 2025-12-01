package com.coder2client.mappers;

import com.coder2client.dtos.UserDto;
import com.coder2client.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDTO(User user) {
        if (user == null) return null;

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public User toEntity(UserDto dto) {
        if (dto == null) return null;

        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .role(dto.getRole() != null ? User.Role.valueOf(dto.getRole()) : User.Role.USER)
                .build();
    }
}
