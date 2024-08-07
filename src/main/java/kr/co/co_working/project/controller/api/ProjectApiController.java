package kr.co.co_working.project.controller.api;

import kr.co.co_working.common.dto.ResponseDto;
import kr.co.co_working.project.dto.ProjectRequestDto;
import kr.co.co_working.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProjectApiController {
    private final ProjectService service;

    /**
     * createProject : Project 등록
     * @param dto
     * @return
     * @throws Exception
     */
    @PostMapping("/api/v1/project")
    public ResponseDto<?> createProject(@RequestBody ProjectRequestDto.CREATE dto) throws Exception {
        service.createProject(dto);

        return ResponseDto.ofSuccess("프로젝트 등록에 성공했습니다.");
    }

    /**
     * updateProject : Project 수정
     * @param id
     * @param dto
     * @return
     * @throws Exception
     */
    @PutMapping("/api/v1/project/{project_id}")
    public ResponseDto<?> updateProject(@PathVariable(name = "project_id") Long id,
                                        @RequestBody ProjectRequestDto.UPDATE dto) throws Exception {
        service.updateProject(id, dto);

        return ResponseDto.ofSuccess("프로젝트 수정에 성공했습니다.");
    }

    /**
     * deleteProject : Project 삭제
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping("/api/v1/project/{project_id}")
    public ResponseDto<?> deleteProject(@PathVariable(name = "project_id") Long id) throws Exception {
        service.deleteProject(id);

        return ResponseDto.ofSuccess("프로젝트 삭제에 성공했습니다.");
    }
}
