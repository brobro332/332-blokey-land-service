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
import xyz.samsami.blokey_land.discipline.domain.BlokeyDiscipline;
import xyz.samsami.blokey_land.discipline.domain.Discipline;
import xyz.samsami.blokey_land.discipline.dto.DisciplineRespDto;
import xyz.samsami.blokey_land.discipline.mapper.DisciplineMapper;
import xyz.samsami.blokey_land.discipline.service.BlokeyDisciplineService;
import xyz.samsami.blokey_land.discipline.service.DisciplineService;
import xyz.samsami.blokey_land.skill.domain.BlokeySkill;
import xyz.samsami.blokey_land.skill.domain.Skill;
import xyz.samsami.blokey_land.skill.dto.SkillRespDto;
import xyz.samsami.blokey_land.skill.mapper.SkillMapper;
import xyz.samsami.blokey_land.skill.service.BlokeySkillService;
import xyz.samsami.blokey_land.skill.service.SkillService;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlokeyService {
    private final SkillService skillService;
    private final BlokeySkillService blokeySkillService;
    private final DisciplineService disciplineService;
    private final BlokeyDisciplineService blokeyDisciplineService;
    private final BlokeyRepository repository;

    @Transactional
    public void createBlokey(BlokeyReqCreateDto dto) {
        Blokey blokey = repository.save(BlokeyMapper.toEntity(dto));

        applySkillsToBlokey(dto, blokey);
        applyDisciplinesToBlokey(dto, blokey);
    }

    public Page<BlokeyRespDto> readBlokeys(Long projectId, Pageable pageable) {
        Page<BlokeyRespDto> page = repository.findByNotInProject(projectId, pageable);

        Set<UUID> blokeyIds = page.getContent().stream()
            .map(BlokeyRespDto::getId)
            .collect(Collectors.toSet());

        if (!blokeyIds.isEmpty()) {
            applySkillsToRespDto(blokeyIds, page);
            applyDisciplinesToRespDto(blokeyIds, page);
        }

        return page;
    }

    public BlokeyRespDto readBlokeyByBlokeyId(UUID blokeyId) {
        return BlokeyMapper.toRespDto(
            repository.findById(blokeyId).orElseThrow(() ->
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

        if (dto.getSkills() != null) applyNewSkillsToReqUpdateDto(dto, blokey);
        if (dto.getDisciplines() != null) applyNewDisciplinesToReqUpdateDto(dto, blokey);
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

    private void applySkillsToBlokey(BlokeyReqCreateDto dto, Blokey blokey) {
        if (dto.getSkills() != null && !dto.getSkills().isEmpty()) {
            for (Long skillId : dto.getSkills()) addSkillToBlokey(blokey, skillId);
        }
    }

    private void applySkillsToRespDto(Set<UUID> blokeyIds, Page<BlokeyRespDto> page) {
        List<BlokeySkill> blokeySkills = blokeySkillService.findByBlokeyIdIn(blokeyIds);

        Map<UUID, Set<SkillRespDto>> skillMap = blokeySkills.stream()
            .collect(
                Collectors.groupingBy(
                    bs -> bs.getBlokey().getId(),
                    Collectors.mapping(bs -> SkillMapper.toRespDto(bs.getSkill()), Collectors.toSet())
                )
            );

        page.getContent().forEach(blokeyRespDto ->
            blokeyRespDto.updateSkills(skillMap.getOrDefault(blokeyRespDto.getId(), Set.of()))
        );
    }

    private void applyNewSkillsToReqUpdateDto(BlokeyReqUpdateDto dto, Blokey blokey) {
        syncCollectionByEntity(
            dto.getSkills(),
            blokey.getSkills(),
            bs -> bs.getSkill().getId(),
            id -> addSkillToBlokey(blokey, id),
            id -> removeSkillFromBlokey(blokey, id)
        );
    }

    @Transactional
    public void addDisciplineToBlokey(Blokey blokey, Long disciplineId) {
        Discipline discipline = disciplineService.findDisciplineByDisciplineId(disciplineId);

        blokeyDisciplineService.create(blokey, discipline);
    }

    @Transactional
    public void removeDisciplineFromBlokey(Blokey blokey, Long disciplineId) {
        Discipline discipline = disciplineService.findDisciplineByDisciplineId(disciplineId);

        blokeyDisciplineService.delete(blokey, discipline);
    }

    private void applyDisciplinesToBlokey(BlokeyReqCreateDto dto, Blokey blokey) {
        if (dto.getDisciplines() != null && !dto.getDisciplines().isEmpty()) {
            for (Long disciplineId : dto.getDisciplines()) addDisciplineToBlokey(blokey, disciplineId);
        }
    }

    private void applyDisciplinesToRespDto(Set<UUID> blokeyIds, Page<BlokeyRespDto> page) {
        List<BlokeyDiscipline> blokeyDisciplines = blokeyDisciplineService.findByBlokeyIdIn(blokeyIds);

        Map<UUID, Set<DisciplineRespDto>> disciplineMap = blokeyDisciplines.stream()
            .collect(
                Collectors.groupingBy(
                    bd -> bd.getBlokey().getId(),
                    Collectors.mapping(bs -> DisciplineMapper.toRespDto(bs.getDiscipline()), Collectors.toSet())
                )
            );

        page.getContent().forEach(blokeyRespDto ->
            blokeyRespDto.updateDisciplines(disciplineMap.getOrDefault(blokeyRespDto.getId(), Set.of()))
        );
    }

    private void applyNewDisciplinesToReqUpdateDto(BlokeyReqUpdateDto dto, Blokey blokey) {
        syncCollectionByEntity(
            dto.getDisciplines(),
            blokey.getDisciplines(),
            bd -> bd.getDiscipline().getId(),
            id -> addDisciplineToBlokey(blokey, id),
            id -> removeDisciplineFromBlokey(blokey, id)
        );
    }

    private <T, ID> void syncCollectionByEntity(
        Collection<ID> newIds,
        Collection<T> existingEntities,
        Function<T, ID> idExtractor,
        Consumer<ID> adder,
        Consumer<ID> remover
    ) {
        Set<ID> newSet = new HashSet<>(newIds);
        Set<ID> existingSet = existingEntities.stream()
            .map(idExtractor)
            .collect(Collectors.toSet());

        for (ID id : newSet) {
            if (!existingSet.contains(id)) adder.accept(id);
        }

        for (ID id : existingSet) {
            if (!newSet.contains(id)) remover.accept(id);
        }
    }
}