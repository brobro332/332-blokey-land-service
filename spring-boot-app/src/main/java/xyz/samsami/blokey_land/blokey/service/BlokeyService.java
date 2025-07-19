package xyz.samsami.blokey_land.blokey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.dto.BlokeyReqCreateDto;
import xyz.samsami.blokey_land.blokey.dto.BlokeyReqUpdateDto;
import xyz.samsami.blokey_land.blokey.dto.BlokeyRespDto;
import xyz.samsami.blokey_land.blokey.mapper.BlokeyMapper;
import xyz.samsami.blokey_land.blokey.repository.BlokeyRepository;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.common.util.StringUtil;
import xyz.samsami.blokey_land.skill.domain.BlokeySkill;
import xyz.samsami.blokey_land.skill.domain.Skill;
import xyz.samsami.blokey_land.skill.dto.SkillRespDto;
import xyz.samsami.blokey_land.skill.mapper.SkillMapper;
import xyz.samsami.blokey_land.skill.service.BlokeySkillService;
import xyz.samsami.blokey_land.skill.service.SkillService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlokeyService {
    private final SkillService skillService;
    private final BlokeySkillService blokeySkillService;
    private final BlokeyRepository repository;

    @Transactional
    public void createBlokey(BlokeyReqCreateDto dto) {
        Blokey blokey = repository.save(BlokeyMapper.toEntity(dto));

        if (dto.getSkills() != null && !dto.getSkills().isEmpty()) {
            for (Long skillId : dto.getSkills()) {
                addSkillToBlokey(blokey, skillId);
            }
        }
    }

    public Page<BlokeyRespDto> readBlokeys(Long projectId, Pageable pageable) {
        Page<BlokeyRespDto> page = repository.findByNotInProject(projectId, pageable);

        Set<UUID> blokeyIds = page.getContent().stream()
            .map(BlokeyRespDto::getId)
            .collect(Collectors.toSet());

        if (!blokeyIds.isEmpty()) {
            List<BlokeySkill> blokeySkills = blokeySkillService.findByBlokeyIdIn(blokeyIds);

            Map<UUID, Set<SkillRespDto>> skillMap = blokeySkills.stream()
                .collect(Collectors.groupingBy(
                    bs -> bs.getBlokey().getId(),
                    Collectors.mapping(bs -> SkillMapper.toRespDto(bs.getSkill()), Collectors.toSet())
                )
            );

            page.getContent().forEach(blokeyRespDto ->
                blokeyRespDto.updateSkillRespDtoSet(skillMap.getOrDefault(blokeyRespDto.getId(), Set.of()))
            );
        }

        return page;
    }

    public BlokeyRespDto readBlokeyByBlokeyId(UUID blokeyId) {
        return BlokeyMapper.toRespDto(
            repository.findByIdWithSkills(blokeyId).orElseThrow(() ->
                new CommonException(ExceptionType.NOT_FOUND, null)
            )
        );
    }

    @Transactional
    public void updateBlokeyByBlokeyId(UUID blokeyId, BlokeyReqUpdateDto dto) {
        Blokey blokey = findBlokeyByBlokeyId(blokeyId);

        if (StringUtil.anyNotNullOrEmpty(dto.getNickname(), dto.getBio())) {
            blokey.updateNickname(dto.getNickname());
            blokey.updateBio(dto.getBio());
        }

        if (dto.getSkills() != null) {
            Set<Long> newSkillIds = new HashSet<>(dto.getSkills());
            Set<Long> existingSkillIds = blokey.getSkills().stream()
                .map(bs -> bs.getSkill().getId())
                .collect(Collectors.toSet());

            for (Long skillId : newSkillIds) {
                if (!existingSkillIds.contains(skillId)) addSkillToBlokey(blokey, skillId);
            }

            for (Long skillId : existingSkillIds) {
                if (!newSkillIds.contains(skillId)) removeSkillFromBlokey(blokey, skillId);
            }
        }
    }

    @Transactional
    public void deleteBlokeyByBlokeyId(UUID blokeyId) {
        Blokey blokey = findBlokeyByBlokeyId(blokeyId);
        repository.delete(blokey);
    }

    public Blokey findBlokeyByBlokeyId(UUID blokeyId) {
        return repository.findById(blokeyId).orElseThrow(() ->
            new CommonException(ExceptionType.NOT_FOUND, null)
        );
    }

    @Transactional
    public void addSkillToBlokey(Blokey blokey, Long skillId) {
        Skill skill = skillService.findSkillBySkillId(skillId);

        blokeySkillService.create(blokey, skill);
    }

    @Transactional
    public void removeSkillFromBlokey(Blokey blokey, Long skillId) {
        Skill skill = skillService.findSkillBySkillId(skillId);

        blokeySkillService.delete(blokey, skill);
    }
}