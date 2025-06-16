package com.example.platform.service;

import com.example.platform.dto.RoleDto;
import com.example.platform.entity.Role;
import com.example.platform.exception.ResourceNotFoundException;
import com.example.platform.mapper.RoleMapper;
import com.example.platform.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Transactional(readOnly = true)
    public List<RoleDto> findAll() {
        log.debug("RoleService:  Fetching all roles");
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoleDto findById(Long id) {
        log.debug("RoleService:  Fetching role by id={}", id);
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + id));
        return roleMapper.toDto(role);
    }

    @Transactional
    public RoleDto create(RoleDto dto) {
        log.info("RoleService:  Creating new role: {}", dto.getName());
        Role role = roleMapper.toEntity(dto);
        Role saved = roleRepository.save(role);
        return roleMapper.toDto(saved);
    }

    @Transactional
    public RoleDto update(Long id, RoleDto dto) {
        log.info("RoleService:  Updating role id={}", id);
        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + id));
        existing.setName(dto.getName());
        Role updated = roleRepository.save(existing);
        return roleMapper.toDto(updated);
    }

    public void delete(Long id) {
        log.info("RoleService:  Deleting role id={}", id);
        roleRepository.deleteById(id);
    }
}
