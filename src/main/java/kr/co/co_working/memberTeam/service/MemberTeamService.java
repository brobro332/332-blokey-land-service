package kr.co.co_working.memberTeam.service;

import kr.co.co_working.member.Member;
import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.repository.MemberRepository;
import kr.co.co_working.member.service.MemberService;
import kr.co.co_working.memberTeam.repository.MemberTeamRepository;
import kr.co.co_working.memberTeam.MemberTeam;
import kr.co.co_working.team.Team;
import kr.co.co_working.team.repository.TeamRepository;
import kr.co.co_working.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberTeamService {
    private final MemberTeamRepository repository;

    /**
     * createMemberTeam : MemberTeam 등록
     * @param member
     * @param team
     * @return
     * @throws Exception
     */
    public Long createMemberTeam(Member member, Team team) throws Exception {
        // 1. MemberTeam 빌드
        MemberTeam memberTeam = MemberTeam.builder()
                .member(member)
                .team(team)
                .build();

        // 2. MemberTeam 등록
        repository.save(memberTeam);

        // 3. ID 반환
        return memberTeam.getId();
    }

    /**
     * updateMemberTeam : MemberTeam 수정
     * @param member
     * @param team
     * @throws NoSuchElementException
     * @throws Exception
     */
    public void updateMemberTeam(Member member, Team team) throws NoSuchElementException, Exception {
        // 1. MemberTeam 조회
        MemberTeam memberTeam = repository.findByMemberEmailAndTeamId(member.getEmail(), team.getId());

        // 2. 부재 시 예외 처리
        if (memberTeam == null) {
            throw new NoSuchElementException("수정하려는 멤버-팀 데이터가 존재하지 않습니다.");
        }

        // 3. MemberTeam 수정
        memberTeam.updateMemberTeam(member, team);
    }

    /**
     * deleteMemberTeam : MemberTeam 삭제
     * @param team
     * @throws NoSuchElementException
     * @throws Exception
     */
    public void deleteMemberTeam(Member member, Team team) throws NoSuchElementException, Exception {
        // 1. MemberTeam 조회
        MemberTeam memberTeam = repository.findByMemberEmailAndTeamId(member.getEmail(), team.getId());

        // 2. 부재 시 예외 처리
        if (memberTeam == null) {
            throw new NoSuchElementException("삭제하려는 멤버-팀 데이터가 존재하지 않습니다.");
        }

        // 3. MemberTeam 삭제
        repository.delete(memberTeam);
    }

    /**
     * deleteMemberTeamByTeamId : teamId 기준으로 MemberTeam 삭제
     * @param team
     * @throws NoSuchElementException
     * @throws Exception
     */
    public void deleteMemberTeamByTeamId(Team team) throws NoSuchElementException, Exception {
        // 1. MemberTeam 조회
        MemberTeam memberTeam = repository.findByTeamId(team.getId());

        // 2. 부재 시 예외 처리
        if (memberTeam == null) {
            throw new NoSuchElementException("삭제하려는 멤버-팀 데이터가 존재하지 않습니다.");
        }

        // 3. MemberTeam 삭제
        repository.delete(memberTeam);
    }
}