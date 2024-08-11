package kr.co.co_working.project.repository;

import kr.co.co_working.project.dto.ProjectRequestDto;
import kr.co.co_working.project.dto.ProjectResponseDto;

import java.util.List;

public interface ProjectDslRepository {
    List<ProjectResponseDto> readProjectList(ProjectRequestDto.READ dto);
}
