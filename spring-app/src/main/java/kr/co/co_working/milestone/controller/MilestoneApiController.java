package kr.co.co_working.milestone.controller;

import kr.co.co_working.common.dto.ResponseDto;
import kr.co.co_working.milestone.dto.MilestoneRequestDto;
import kr.co.co_working.milestone.service.MilestoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class MilestoneApiController {
    private final MilestoneService service;

    /**
     * createMilestone : Milestone 등록
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @PostMapping("/api/v1/milestone")
    public ResponseDto<?> createMilestone(MilestoneRequestDto.CREATE dto) throws NoSuchElementException, Exception {
        service.createMilestone(dto);

        return ResponseDto.ofSuccess("마일스톤 등록에 성공하였습니다.");
    }

    /**
     * readMilestoneList : Milestone 조회
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @GetMapping("/api/v1/milestone")
    public ResponseDto<?> readMilestoneList(MilestoneRequestDto.READ dto) throws NoSuchElementException, Exception {
        return ResponseDto.ofSuccess("마일스톤 조회에 성공하였습니다.");
    }

    /**
     * updateMilestone : Milestone 수정
     * @param dto
     * @return
     * @throws NoSuchFieldException
     * @throws Exception
     */
    @PutMapping("/api/v1/milestone")
    public ResponseDto<?> updateMilestone(MilestoneRequestDto.UPDATE dto) throws NoSuchFieldException, Exception {
        service.updateMilestone(dto);

        return ResponseDto.ofSuccess("마일스톤 수정에 성공하였습니다.");
    }

    /**
     * deleteMilestone : Milestone 삭제
     * @param dto
     * @return
     * @throws NoSuchFieldException
     * @throws Exception
     */
    @DeleteMapping("/api/v1/milestone")
    public ResponseDto<?> deleteMilestone(MilestoneRequestDto.DELETE dto) throws NoSuchFieldException, Exception {
        service.deleteMilestone(dto);

        return ResponseDto.ofSuccess("마일스톤 삭제에 성공하였습니다.");
    }
}