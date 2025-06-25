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

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlokeyService {
    private final BlokeyRepository repository;

    @Transactional
    public void createBlokey(BlokeyReqCreateDto dto) {
        repository.save(BlokeyMapper.toEntity(dto));
    }

    public Page<BlokeyRespDto> readBlokeys(Pageable pageable) {
        return repository.findAll(pageable)
            .map(BlokeyMapper::toRespDto);
    }

    public BlokeyRespDto readBlokeyByBlokeyId(UUID blokeyId) {
        return BlokeyMapper.toRespDto(findBlokeyByBlokeyId(blokeyId));
    }

    @Transactional
    public void updateBlokeyByBlokeyId(UUID blokeyId, BlokeyReqUpdateDto dto) {
        if (StringUtil.anyNotNullOrEmpty(dto.getNickname(), dto.getBio())) {
            Blokey blokey = findBlokeyByBlokeyId(blokeyId);
            blokey.updateNickname(dto.getNickname());
            blokey.updateBio(dto.getBio());
        }
    }

    @Transactional
    public void deleteBlokeyByBlokeyId(UUID blokeyId) {
        Blokey blokey = findBlokeyByBlokeyId(blokeyId);
        if (blokey != null) repository.delete(blokey);
    }

    public Blokey findBlokeyByBlokeyId(UUID blokeyId) {
        return repository.findById(blokeyId).orElseThrow(() ->
            new CommonException(ExceptionType.NOT_FOUND, null)
        );
    }
}