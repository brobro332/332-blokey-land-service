package xyz.samsami.blokey_land.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import xyz.samsami.blokey_land.common.dto.CommonRespDto;
import xyz.samsami.blokey_land.member.dto.MemberReqCreateDto;
import xyz.samsami.blokey_land.member.dto.MemberReqDeleteDto;
import xyz.samsami.blokey_land.member.dto.MemberReqUpdateDto;
import xyz.samsami.blokey_land.member.dto.MemberRespDto;

import java.util.UUID;

@RequestMapping("/api")
@Tag(name = "Member API", description = "멤버 관련 API")
public interface MemberApi {
    @Operation(summary = "멤버 생성", description = "프로젝트에 멤버를 등록합니다.")
    @PostMapping("/projects/{projectId}/members")
    CommonRespDto<Void> createMember(
        @PathVariable Long projectId,
        @RequestBody MemberReqCreateDto dto
    );

    @Operation(summary = "프로젝트별 멤버 목록 조회", description = "프로젝트 도메인에서 멤버 목록을 조회합니다.")
    @GetMapping("/projects/{projectId}/members")
    CommonRespDto<Page<MemberRespDto>> readMembersByProjectId(
        @PathVariable Long projectId,
        @Parameter(hidden = true) Pageable pageable
    );

    @Operation(summary = "유저별 멤버 목록 조회", description = "사용자 도메인에서 멤버 목록을 조회합니다.")
    @GetMapping("/users/{userId}/members")
    CommonRespDto<Page<MemberRespDto>> readMembersByUserId(
        @PathVariable UUID userId,
        @Parameter(hidden = true) Pageable pageable
    );

    @Operation(summary = "멤버 정보 수정", description = "멤버 정보를 수정합니다.")
    @PatchMapping("/members/{memberId}")
    CommonRespDto<Void> updateMembersByMemberId(
        @PathVariable Long memberId,
        @RequestBody MemberReqUpdateDto dto
    );

    @Operation(summary = "멤버 삭제", description = "프로젝트 도메인에서 멤버를 삭제합니다.")
    @DeleteMapping("/projects/{projectId}/members")
    CommonRespDto<Void> deleteMember(
        @PathVariable Long projectId,
        @RequestBody MemberReqDeleteDto dto
    );
}