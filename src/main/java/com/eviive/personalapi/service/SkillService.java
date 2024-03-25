package com.eviive.personalapi.service;

import com.eviive.personalapi.dto.SkillDTO;
import com.eviive.personalapi.dto.SortUpdateDTO;
import com.eviive.personalapi.entity.Skill;
import com.eviive.personalapi.exception.PersonalApiException;
import com.eviive.personalapi.mapper.SkillMapper;
import com.eviive.personalapi.repository.SkillRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.API404_SKILL_ID_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class SkillService {

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

        Integer newSort = skillRepository
                .findMaxSort()
                .map(sort -> sort + 1)
                .orElse(0);

        skill.setSort(newSort);

        return saveOrUpdate(skill, file);
    }

    private SkillDTO saveOrUpdate(Skill skill, MultipartFile file) {
        UUID oldUuid = null;
        if (file != null) {
            oldUuid = skill.getImage().getUuid();
            skill.getImage().setUuid(UUID.randomUUID());
        }

        Skill savedSkill = skillRepository.save(skill);

        if (file != null) {
            imageService.upload(savedSkill.getImage(), oldUuid, file);
        }

        return skillMapper.toDTO(savedSkill);
    }

    public void sort(List<SortUpdateDTO> sorts) {
        for (SortUpdateDTO sort: sorts) {
            skillRepository.updateSortById(sort.getId(), sort.getSort());
        }
    }

    public SkillDTO update(Long id, SkillDTO skillDTO, @Nullable MultipartFile file) {
        if (!skillRepository.existsById(id)) {
            throw PersonalApiException.format(API404_SKILL_ID_NOT_FOUND, id);
        }

        Skill skill = skillMapper.toEntity(skillDTO);

        skill.setId(id);

        return saveOrUpdate(skill, file);
    }

    public void delete(Long id) {
        Skill skill = skillRepository.findById(id)
                                     .orElseThrow(() -> PersonalApiException.format(API404_SKILL_ID_NOT_FOUND, id));

        if (skill.getImage().getUuid() != null) {
            imageService.delete(skill.getImage());
        }

        skill.getProjects().forEach(project -> project.getSkills().remove(skill));

        skillRepository.deleteById(id);
    }

}
