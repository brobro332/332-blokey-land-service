package kr.co.co_working.member.controller;

import kr.co.co_working.common.dto.ResponseDto;
import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.dto.MemberResponseDto;
import kr.co.co_working.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    /**
     * readMemberList : Member 조회
     * @param dto
     * @return
     * @throws Exception
     */
    @GetMapping("/api/v1/member")
    public ResponseDto<?> readMemberList(@RequestBody MemberRequestDto.READ dto) throws Exception {
        return ResponseDto.ofSuccess("멤버 조회에 성공했습니다.", service.readMemberList(dto));
    }

    /**
     * updateMember : Member 수정
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @PutMapping("/api/v1/member")
    public ResponseDto<?> updateMember(@RequestBody MemberRequestDto.UPDATE dto) throws NoSuchElementException, Exception {
        service.updateMember(dto);

        return ResponseDto.ofSuccess("멤버 수정에 성공했습니다.");
    }

    /**
     * deleteMember : Member 삭제
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @DeleteMapping("/api/v1/member")
    public ResponseDto<?> deleteMember(@RequestBody MemberRequestDto.DELETE dto) throws NoSuchElementException, Exception {
        service.deleteMember(dto);
        
        return ResponseDto.ofSuccess("멤버 삭제에 성공했습니다.");
    }
}