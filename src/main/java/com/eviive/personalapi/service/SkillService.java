package com.eviive.personalapi.service;

import com.eviive.personalapi.dto.SkillDTO;
import com.eviive.personalapi.entity.Skill;
import com.eviive.personalapi.exception.PersonalApiException;
import com.eviive.personalapi.mapper.SkillMapper;
import com.eviive.personalapi.repository.SkillRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.API404_SKILL_ID_NOT_FOUND;

@Service
@Data
@AllArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    public SkillDTO findById(Long id) {
        Skill skill = skillRepository.findById(id)
                                     .orElseThrow(() -> PersonalApiException.format(API404_SKILL_ID_NOT_FOUND, id));
        return skillMapper.toDTO(skill);
    }

    public List<SkillDTO> findAll() {
        return skillMapper.toListDTO(skillRepository.findAll());
    }

    public SkillDTO save(SkillDTO skillDTO) {
        Skill skill = skillMapper.toEntity(skillDTO);

        return skillMapper.toDTO(skillRepository.save(skill));
    }

    public SkillDTO update(Long id, SkillDTO skillDTO) {
        if (!skillRepository.existsById(id)) {
            throw PersonalApiException.format(API404_SKILL_ID_NOT_FOUND, id);
        }

        Skill skill = skillMapper.toEntity(skillDTO);

        skill.setId(id);

        return skillMapper.toDTO(skillRepository.save(skill));
    }

    public void delete(Long id) {
        if (!skillRepository.existsById(id)) {
            throw PersonalApiException.format(API404_SKILL_ID_NOT_FOUND, id);
        }

        skillRepository.deleteById(id);
    }

}
