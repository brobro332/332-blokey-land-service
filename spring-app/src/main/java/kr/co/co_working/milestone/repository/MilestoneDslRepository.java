package kr.co.co_working.milestone.repository;

import kr.co.co_working.milestone.dto.MilestoneRequestDto;
import kr.co.co_working.milestone.dto.MilestoneResponseDto;

import java.util.List;

public interface MilestoneDslRepository {
    List<MilestoneResponseDto> readMilestoneList(MilestoneRequestDto.READ dto);
}
