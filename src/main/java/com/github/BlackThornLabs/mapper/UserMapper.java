package com.github.BlackThornLabs.mapper;

import com.github.BlackThornLabs.dto.UserRequestDTO;
import com.github.BlackThornLabs.dto.UserResponseDTO;
import com.github.BlackThornLabs.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toResponseDTO(User user);

    @Mapping(target = "id",  ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(UserRequestDTO userRequestDTO);
}
