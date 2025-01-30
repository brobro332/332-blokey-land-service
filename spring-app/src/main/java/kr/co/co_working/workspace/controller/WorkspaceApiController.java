package kr.co.co_working.workspace.controller;

import kr.co.co_working.common.dto.ResponseDto;
import kr.co.co_working.workspace.dto.WorkspaceRequestDto;
import kr.co.co_working.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WorkspaceApiController {
    private final WorkspaceService service;

    /**
     * createWorkspace : Workspace 등록
     * @param dto
     * @return
     * @throws Exception
     */
    @PostMapping("/api/v1/workspace")
    public ResponseDto<?> createWorkspace(@RequestBody WorkspaceRequestDto.CREATE dto) throws Exception {
        service.createWorkspace(dto);

        return ResponseDto.ofSuccess("워크스페이스 등록에 성공했습니다.");
    }

    /**
     * readWorkspace : Workspace 조회
     * @param dto
     * @return
     * @throws Exception
     */
    @GetMapping("/api/v1/workspace")
    public ResponseDto<?> readWorkspace(@ModelAttribute WorkspaceRequestDto.READ dto) throws Exception {
        return ResponseDto.ofSuccess("워크스페이스 조회에 성공했습니다.", service.readWorkspace(dto));
    }

    /**
     * updateWorkspace : Workspace 수정
     * @param id
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @PutMapping("/api/v1/workspace/{workspace_id}")
    public ResponseDto<?> updateWorkspace(@PathVariable(name = "workspace_id") Long id,
                                     @RequestBody WorkspaceRequestDto.UPDATE dto) throws NoSuchElementException, Exception {
        service.updateWorkspace(id, dto);

        return ResponseDto.ofSuccess("워크스페이스 수정에 성공했습니다.");
    }

    /**
     * deleteWorkspace : Workspace 삭제
     * @param id
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @DeleteMapping("/api/v1/workspace/{workspace_id}")
    public ResponseDto<?> deleteWorkspace(@PathVariable(name = "workspace_id") Long id) throws NoSuchElementException, Exception {
        service.deleteWorkspace(id);

        return ResponseDto.ofSuccess("워크스페이스 삭제에 성공했습니다.");
    }

    /**
     * addMemberToWorkspace : Workspace 데이터에 Member 추가
     * @param email
     * @param teamId
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @DeleteMapping("/api/v1/workspace/{workspace_id}/{email}")
    public ResponseDto<?> addMemberToWorkspace(@PathVariable(name="workspace_id") Long teamId,
                                            @PathVariable(name="email") String email) throws NoSuchElementException, Exception {
        service.addMemberToWorkspace(teamId, email);

        return ResponseDto.ofSuccess("멤버 추가에 성공하였습니다.");
    }

    /**
     * removeMemberFromWorkspace : Workspace 데이터에서 Member 제외
     * @param email
     * @param teamId
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @DeleteMapping("/api/v1/workspace/{email}/{workspace_id}")
    public ResponseDto<?> removeMemberFromWorkspace(@PathVariable(name="email") String email,
                                               @PathVariable(name="workspace_id") Long teamId) throws NoSuchElementException, Exception {
        service.removeMemberFromWorkspace(email, teamId);

        return ResponseDto.ofSuccess("멤버 제외에 성공하였습니다.");
    }
}
