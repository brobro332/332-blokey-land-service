package xyz.samsami.blokey_land.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import xyz.samsami.blokey_land.common.dto.CommonRespDto;
import xyz.samsami.blokey_land.common.type.ResultType;
import xyz.samsami.blokey_land.member.dto.MemberReqCreateDto;
import xyz.samsami.blokey_land.member.dto.MemberReqDeleteDto;
import xyz.samsami.blokey_land.member.dto.MemberReqUpdateDto;
import xyz.samsami.blokey_land.member.dto.MemberRespDto;
import xyz.samsami.blokey_land.member.service.MemberService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService service;

    @PostMapping("/projects/{projectId}/members")
    public CommonRespDto<Void> createMember(@PathVariable Long projectId, @RequestBody MemberReqCreateDto dto) {
        service.createMember(projectId, dto);
        return CommonRespDto.of(ResultType.SUCCESS, "멤버 등록 완료", null);
    }

    @GetMapping("/projects/{projectId}/members")
    public CommonRespDto<Page<MemberRespDto>> readMembersByProjectId(
        @PathVariable Long projectId,
        @PageableDefault(
            sort = "memberId", direction = Sort.Direction.DESC
        ) Pageable pageable
    ) {
        Page<MemberRespDto> page = service.readMemberByProjectId(projectId, pageable);
        return CommonRespDto.of(ResultType.SUCCESS, "멤버 목록 조회 완료", page);
    }

    @GetMapping("/users/{userId}/members")
    public CommonRespDto<Page<MemberRespDto>> readMembersByUserId(
        @PathVariable UUID userId,
        @PageableDefault(
                sort = "memberId", direction = Sort.Direction.DESC
        ) Pageable pageable
    ) {
        Page<MemberRespDto> page = service.readMemberByUserId(userId, pageable);
        return CommonRespDto.of(ResultType.SUCCESS, "멤버 목록 조회 완료", null);
    }

    @PatchMapping("/members/{memberId}")
    public CommonRespDto<Void> updateMembersByMemberId(@PathVariable Long memberId, @RequestBody MemberReqUpdateDto dto) {
        service.updateMemberByMemberId(memberId, dto);
        return CommonRespDto.of(ResultType.SUCCESS, "멤버 정보 수정 완료", null);
    }

    @DeleteMapping("/projects/{projectId}/members")
    public CommonRespDto<Void> deleteMember(@PathVariable Long projectId, @RequestBody MemberReqDeleteDto dto) {
        service.deleteMember(projectId, dto);
        return CommonRespDto.of(ResultType.SUCCESS, "멤버 정보 삭제 완료", null);
    }
}