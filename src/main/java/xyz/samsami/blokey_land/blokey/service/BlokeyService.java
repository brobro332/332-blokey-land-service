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
import xyz.samsami.blokey_land.blokey.vo.AccountVo;
import xyz.samsami.blokey_land.common.dto.CommonRespDto;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.common.util.StringUtil;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlokeyService {
    private final BlokeyHelperService helperService;
    private final BlokeyRepository repository;

    @Transactional
    public void createBlokey(BlokeyReqCreateDto dto) {
        CommonRespDto<AccountVo> response = helperService.createBlokeyOnAuthenticationServer(dto);
        repository.save(BlokeyMapper.toEntity(dto, response.getData().accountId()));
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
    public void softDeleteBlokeyByBlokeyId(UUID blokeyId) {
        Blokey blokey = findBlokeyByBlokeyId(blokeyId);
        if (blokey != null) blokey.updateDeleted(true);
    }

    @Transactional
    public void restoreBlokeyByBlokeyId(UUID blokeyId) {
        Blokey blokey = findBlokeyByBlokeyId(blokeyId);
        if (blokey != null) blokey.updateDeleted(false);
    }

    public Blokey findBlokeyByBlokeyId(UUID blokeyId) {
        return repository.findById(blokeyId).orElseThrow(() ->
            new CommonException(ExceptionType.NOT_FOUND, null)
        );
    }
}