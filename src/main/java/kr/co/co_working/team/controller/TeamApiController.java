package kr.co.co_working.team.controller;

import kr.co.co_working.common.dto.ResponseDto;
import kr.co.co_working.team.dto.TeamRequestDto;
import kr.co.co_working.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class TeamApiController {
    private final TeamService service;

    /**
     * createTeam : Team 등록
     * @param dto
     * @return
     * @throws Exception
     */
    @PostMapping("/api/v1/team")
    public ResponseDto<?> createTeam(@RequestBody TeamRequestDto.CREATE dto) throws Exception {
        service.createTeam(dto);

        return ResponseDto.ofSuccess("팀 등록에 성공했습니다.");
    }

    /**
     * readTeam : Team 조회
     * @param dto
     * @return
     * @throws Exception
     */
    @GetMapping("/api/v1/team")
    public ResponseDto<?> readTeam(@RequestBody TeamRequestDto.READ dto) throws Exception {
        return ResponseDto.ofSuccess("팀 조회에 성공했습니다.", service.readTeam(dto));
    }

    /**
     * updateTeam : Team 수정
     * @param id
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @PutMapping("/api/v1/team/{team_id}")
    public ResponseDto<?> updateTeam(@PathVariable(name = "team_id") Long id,
                                     @RequestBody TeamRequestDto.UPDATE dto) throws NoSuchElementException, Exception {
        service.updateTeam(id, dto);

        return ResponseDto.ofSuccess("팀 수정에 성공했습니다.");
    }

    /**
     * deleteTeam : Team 삭제
     * @param id
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @DeleteMapping("/api/v1/team/{team_id}")
    public ResponseDto<?> deleteTeam(@PathVariable(name = "team_id") Long id) throws NoSuchElementException, Exception {
        service.deleteTeam(id);

        return ResponseDto.ofSuccess("팀 삭제에 성공했습니다.");
    }
}
