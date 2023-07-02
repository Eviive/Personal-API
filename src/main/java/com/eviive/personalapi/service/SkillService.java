package com.eviive.personalapi.service;

import com.eviive.personalapi.dto.SkillDTO;
import com.eviive.personalapi.entity.Skill;
import com.eviive.personalapi.exception.PersonalApiException;
import com.eviive.personalapi.mapper.SkillMapper;
import com.eviive.personalapi.repository.SkillRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.API404_SKILL_ID_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SkillService {

    private static final String AZURE_CONTAINER_NAME = "skill-images";

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    private final ImageService imageService;

    public SkillDTO findById(Long id) {
        Skill skill = skillRepository.findById(id)
                                     .orElseThrow(() -> PersonalApiException.format(API404_SKILL_ID_NOT_FOUND, id));
        return skillMapper.toDTO(skill);
    }

    public List<SkillDTO> findAll() {
        return skillMapper.toListDTO(skillRepository.findAll());
    }

    public SkillDTO save(SkillDTO skillDTO, @Nullable MultipartFile file) {
        Skill skill = skillMapper.toEntity(skillDTO);

        if (file != null) {
            imageService.upload(skill.getImage(), AZURE_CONTAINER_NAME, file);
        }

        return skillMapper.toDTO(skillRepository.save(skill));
    }

    public SkillDTO update(Long id, SkillDTO skillDTO, @Nullable MultipartFile file) {
        if (!skillRepository.existsById(id)) {
            throw PersonalApiException.format(API404_SKILL_ID_NOT_FOUND, id);
        }

        Skill skill = skillMapper.toEntity(skillDTO);

        skill.setId(id);

        if (file != null) {
            imageService.upload(skill.getImage(), AZURE_CONTAINER_NAME, file);
        }

        return skillMapper.toDTO(skillRepository.save(skill));
    }

    public void delete(Long id) {
        if (!skillRepository.existsById(id)) {
            throw PersonalApiException.format(API404_SKILL_ID_NOT_FOUND, id);
        }

        skillRepository.deleteById(id);
    }

}
