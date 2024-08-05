package kr.co.co_working.project.controller.api;

import kr.co.co_working.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectApiController {
    private final ProjectService service;

}
