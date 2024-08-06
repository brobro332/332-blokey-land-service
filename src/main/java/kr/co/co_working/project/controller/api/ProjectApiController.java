package kr.co.co_working.project.controller.api;

import kr.co.co_working.common.dto.ResponseDto;
import kr.co.co_working.project.dto.ProjectRequestDto;
import kr.co.co_working.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
