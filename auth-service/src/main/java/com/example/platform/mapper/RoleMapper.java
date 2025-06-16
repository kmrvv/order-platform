package com.example.platform.mapper;

import com.example.platform.dto.RoleDto;
import com.example.platform.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {
    RoleDto toDto(Role entity);
    Role toEntity(RoleDto dto);
}
