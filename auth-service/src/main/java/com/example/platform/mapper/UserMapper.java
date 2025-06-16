package com.example.platform.mapper;

import com.example.platform.dto.UserDto;
import com.example.platform.entity.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(userDto.getPassword()))")
    @Mapping(target = "enabled", constant = "true")
    User toEntity(UserDto userDto, @Context PasswordEncoder passwordEncoder);
}
