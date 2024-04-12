package com.eviive.personalapi.service;

import com.eviive.personalapi.dto.RoleDTO;
import com.eviive.personalapi.entity.Role;
import com.eviive.personalapi.exception.PersonalApiException;
import com.eviive.personalapi.mapper.RoleMapper;
import com.eviive.personalapi.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.API404_ROLE_ID_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public RoleDTO findById(final Long id) {
        final Role role = roleRepository.findById(id)
            .orElseThrow(() -> PersonalApiException.format(API404_ROLE_ID_NOT_FOUND, id));
        return roleMapper.toDTO(role);
    }

    public List<RoleDTO> findAll() {
        return roleMapper.toListDTO(roleRepository.findAll());
    }

}
