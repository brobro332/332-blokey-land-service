package kr.co.co_working.invitation.controller;

import kr.co.co_working.common.dto.ResponseDto;
import kr.co.co_working.invitation.dto.InvitationRequestDto;
import kr.co.co_working.invitation.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class InvitationApiController {
    private final InvitationService service;

    /**
     * createInvitation : 워크스페이스 가입요청 생성
     *
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @PostMapping
    public ResponseDto<?> createInvitation(InvitationRequestDto.CREATE dto) throws NoSuchElementException, Exception {
        service.createInvitation(dto);
        return ResponseDto.ofSuccess("워크스페이스 가입요청 등록에 성공했습니다.");
    }

    /**
     * deleteInvitation : 워크스페이스 가입요청 삭제
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @DeleteMapping
    public ResponseDto<?> deleteInvitation(InvitationRequestDto.DELETE dto) throws NoSuchElementException, Exception  {
        service.deleteInvitation(dto);
        return ResponseDto.ofSuccess("워크스페이스 가입요청 삭제에 성공했습니다.");
    }
}