package kr.co.co_working.invitation.controller;

import kr.co.co_working.common.dto.ResponseDto;
import kr.co.co_working.invitation.dto.InvitationRequestDto;
import kr.co.co_working.invitation.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class InvitationApiController {
    private final InvitationService service;

    /**
     * createInvitation : Invitation 생성
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @PostMapping("/api/v1/invitation")
    public ResponseDto<?> createInvitation(@RequestBody InvitationRequestDto.CREATE dto) throws NoSuchElementException, Exception {
        service.createInvitation(dto);
        return ResponseDto.ofSuccess("워크스페이스 가입요청 등록에 성공했습니다.");
    }

    /**
     * readInvitation : Invitation 조회
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @GetMapping("/api/v1/invitation")
    public ResponseDto<?> readInvitation(@ModelAttribute InvitationRequestDto.READ dto) throws NoSuchElementException, Exception {
        return ResponseDto.ofSuccess("워크스페이스 가입요청 조회에 성공했습니다.", service.readInvitation(dto));
    }

    /**
     * deleteInvitation : Invitation 삭제
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @DeleteMapping("/api/v1/invitation")
    public ResponseDto<?> deleteInvitation(@RequestBody InvitationRequestDto.DELETE dto) throws NoSuchElementException, Exception  {
        service.deleteInvitation(dto);
        return ResponseDto.ofSuccess("워크스페이스 가입요청 삭제에 성공했습니다.");
    }
}