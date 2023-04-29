package com.eviive.personalapi.service;

import com.eviive.personalapi.dto.RoleDTO;
import com.eviive.personalapi.entity.Role;
import com.eviive.personalapi.exception.PersonalApiException;
import com.eviive.personalapi.mapper.RoleMapper;
import com.eviive.personalapi.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.API404_ROLE_ID_NOT_FOUND;

@Service
@Data
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleDTO findById(Long id) {
        Role role = roleRepository.findById(id)
                                     .orElseThrow(() -> PersonalApiException.format(API404_ROLE_ID_NOT_FOUND, id));
        return roleMapper.toDTO(role);
    }

    public List<RoleDTO> findAll() {
        return roleMapper.toListDTO(roleRepository.findAll());
    }

}
