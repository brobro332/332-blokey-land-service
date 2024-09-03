package kr.co.co_working.memberTeam;

import kr.co.co_working.common.dto.ResponseDto;
import kr.co.co_working.memberTeam.service.MemberTeamService;
import kr.co.co_working.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class MemberTeamController {
    private final MemberTeamService service;
    private final TeamService teamService;

    /**
     * deleteMemberFromTeam : Team 데이터에서 Member 제외
     * @param email
     * @param teamId
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    @DeleteMapping("/api/v1/memberTeam/{email}/{team_id}")
    public ResponseDto<?> deleteMemberFromTeam(@PathVariable(name="email") String email,
                                               @PathVariable(name="team_id") Long teamId) throws NoSuchElementException, Exception {
        teamService.deleteMemberFromTeam(email, teamId);

        return ResponseDto.ofSuccess("팀에서 멤버 제외에 성공하였습니다.");
    }
}
