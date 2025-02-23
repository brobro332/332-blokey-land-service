package kr.co.co_working.workspace.repository;

import kr.co.co_working.workspace.dto.WorkspaceRequestDto;
import kr.co.co_working.workspace.dto.WorkspaceResponseDto;

import java.util.List;

public interface WorkspaceDslRepository {
    List<WorkspaceResponseDto> readWorkspaceList(WorkspaceRequestDto.READ dto);
    List<WorkspaceResponseDto> readWorkspaceListNotJoined(WorkspaceRequestDto.READ dto);
}