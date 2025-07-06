package xyz.samsami.blokey_land.offer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import xyz.samsami.blokey_land.common.dto.CommonRespDto;
import xyz.samsami.blokey_land.common.type.ResultType;
import xyz.samsami.blokey_land.offer.dto.OfferReqCreateDto;
import xyz.samsami.blokey_land.offer.dto.OfferReqReadDto;
import xyz.samsami.blokey_land.offer.dto.OfferReqUpdateDto;
import xyz.samsami.blokey_land.offer.dto.OfferRespDto;
import xyz.samsami.blokey_land.offer.service.OfferService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OfferController implements OfferApi {
    private final OfferService service;

    @Override
        public CommonRespDto<OfferRespDto> createOffer(
        @RequestBody OfferReqCreateDto dto
    ) {
        OfferRespDto response = service.createOffer(dto);
        return CommonRespDto.of(ResultType.SUCCESS, "제안 등록 완료", response);
    }

    @Override
    public CommonRespDto<Page<OfferRespDto>> readOffers(
        @ModelAttribute OfferReqReadDto dto,
        @PageableDefault(
            sort = "id", direction = Sort.Direction.DESC
        ) Pageable pageable
    ) {
        Page<OfferRespDto> page = service.readOffers(dto, pageable);
        return CommonRespDto.of(ResultType.SUCCESS, "제안 목록 조회 완료", page);
    }

    @Override
    public CommonRespDto<Void> updateOffer(@PathVariable Long offerId, @RequestBody OfferReqUpdateDto dto) {
        service.updateOffer(offerId, dto);
        return CommonRespDto.of(ResultType.SUCCESS, "제안 정보 수정 완료", null);
    }

    @Override
    public CommonRespDto<Void> deleteOffer(@PathVariable Long offerId) {
        service.deleteOffer(offerId);
        return CommonRespDto.of(ResultType.SUCCESS, "제안 삭제 완료", null);
    }
}