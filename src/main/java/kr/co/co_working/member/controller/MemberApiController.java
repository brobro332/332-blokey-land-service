package kr.co.co_working.member.controller;

import kr.co.co_working.common.dto.ResponseDto;
import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService service;

    /**
     * createMember : Member 등록
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @PostMapping("/api/v1/member")
    public ResponseDto<?> createMember(@RequestBody MemberRequestDto.CREATE dto) throws NoSuchElementException, Exception {
        service.createMember(dto);

        return ResponseDto.ofSuccess("멤버 등록에 성공했습니다.");
    }
}