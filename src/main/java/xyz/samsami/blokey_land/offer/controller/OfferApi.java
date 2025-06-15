package xyz.samsami.blokey_land.offer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import xyz.samsami.blokey_land.common.dto.CommonRespDto;
import xyz.samsami.blokey_land.offer.dto.OfferReqCreateDto;
import xyz.samsami.blokey_land.offer.dto.OfferReqReadDto;
import xyz.samsami.blokey_land.offer.dto.OfferReqUpdateDto;
import xyz.samsami.blokey_land.offer.dto.OfferRespDto;

import java.util.UUID;

@RequestMapping("/api/offers")
@Tag(name = "Offer API", description = "제안 관련 API")
public interface OfferApi {
    @Operation(summary = "제안 등록", description = "제안을 등록합니다.")
    @PostMapping
    CommonRespDto<Void> createOffer(
        @RequestParam Long projectId,
        @RequestParam UUID blokeyId,
        @RequestBody OfferReqCreateDto dto
    );

    @Operation(summary = "제안 목록 조회", description = "제안 목록을 조회합니다.")
    @GetMapping
    CommonRespDto<Page<OfferRespDto>> readOffers(
        @RequestBody OfferReqReadDto dto,
        @Parameter(hidden = true) @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    );

    @Operation(summary = "제안 수정", description = "제안 정보를 수정합니다.")
    @PatchMapping("/{offerId}")
    CommonRespDto<Void> updateOffer(
        @PathVariable Long offerId,
        @RequestBody OfferReqUpdateDto dto
    );

    @Operation(summary = "제안 삭제", description = "제안을 삭제합니다.")
    @DeleteMapping("/{offerId}")
    CommonRespDto<Void> deleteOffer(
        @PathVariable Long offerId
    );
}