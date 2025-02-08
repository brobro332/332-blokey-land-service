package kr.co.co_working.member.controller;

import kr.co.co_working.common.dto.ResponseDto;
import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.service.MemberService;
import kr.co.co_working.workspace.dto.WorkspaceRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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
     * @return
     * @throws Exception
     */
    @GetMapping("/api/v1/member")
    public ResponseDto<?> readMember() throws Exception {
        return ResponseDto.ofSuccess("멤버 조회에 성공했습니다.", service.readMember());
    }

    /**
     * readMembers : Members 조회
     * @param dto
     * @return
     * @throws Exception
     */
    @GetMapping("/api/v1/members")
    public ResponseDto<?> readMembers(@RequestBody MemberRequestDto.READ dto) throws Exception {
        return ResponseDto.ofSuccess("멤버목록 조회에 성공했습니다.", service.readMembers(dto));
    }

    /**
     * readMembersInWorkspace : 특정 Workspace 소속 Members 조회
     * @param dto
     * @return
     * @throws Exception
     */
    @GetMapping("/api/v1/workspace/members-in-workspace")
    public ResponseDto<?> readMembersInWorkspace(@ModelAttribute WorkspaceRequestDto.READ dto) throws Exception {
        return ResponseDto.ofSuccess("멤버목록 조회에 성공했습니다.", service.readMembersInWorkspace(dto));
    }

    /**
     * readMembersNotInWorkspace : 특정 Workspace 미가입 Members 조회
     * @param dto
     * @return
     * @throws Exception
     */
    @GetMapping("/api/v1/workspace/members-not-in-workspace")
    public ResponseDto<?> readMembersNotInWorkspace(@ModelAttribute WorkspaceRequestDto.READ dto) throws Exception {
        return ResponseDto.ofSuccess("멤버목록 조회에 성공했습니다.", service.readMembersNotInWorkspace(dto));
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
     * updatePassword : Member 비밀번호 수정
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @PutMapping("/api/v1/member/password")
    public ResponseDto<?> updatePassword(@RequestBody MemberRequestDto.PASSWORD dto) throws NoSuchElementException, Exception {
        service.updatePassword(dto);

        return ResponseDto.ofSuccess("멤버 비밀번호 수정에 성공했습니다.");
    }

    /**
     * deleteMember : Member 삭제
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @DeleteMapping("/api/v1/member")
    public ResponseDto<?> deleteMember() throws NoSuchElementException, Exception {
        service.deleteMember();
        
        return ResponseDto.ofSuccess("멤버 삭제에 성공했습니다.");
    }
}