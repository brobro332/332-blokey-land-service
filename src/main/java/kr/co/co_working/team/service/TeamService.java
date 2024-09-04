package kr.co.co_working.team.service;

import kr.co.co_working.member.Member;
import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.repository.MemberRepository;
import kr.co.co_working.member.service.MemberService;
import kr.co.co_working.memberTeam.service.MemberTeamService;
import kr.co.co_working.team.Team;
import kr.co.co_working.team.dto.TeamRequestDto;
import kr.co.co_working.team.dto.TeamResponseDto;
import kr.co.co_working.team.repository.TeamDslRepository;
import kr.co.co_working.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final MemberService memberService;

    private final MemberTeamService memberTeamService;

    private final TeamRepository repository;

    private final MemberRepository memberRepository;

    private final TeamDslRepository dslRepository;

    /**
     * createTeam : Team 등록
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    public Long createTeam(TeamRequestDto.CREATE dto) throws NoSuchElementException, Exception {
        // 1. Member 조회
        Optional<Member> selectedMember = memberRepository.findById(dto.getEmail());

        // 2. 부재 시 예외 처리
        if (selectedMember.isEmpty()) {
            throw new NoSuchElementException("등록하려는 멤버가 존재하지 않습니다.");
        }

        // 3. Team 빌드
        Team team = Team.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        // 4. Member 추출
        Member member = selectedMember.get();

        // 5. Team, MemberTeam 등록
        repository.save(team);
        memberTeamService.createMemberTeam(member, team);

        // 6. ID 반환
        return team.getId();
    }

    /**
     * readTeam : Team 조회
     * @param dto
     * @return
     * @throws Exception
     */
    public List<TeamResponseDto> readTeam(TeamRequestDto.READ dto) throws Exception {
        // QueryDSL 동적 쿼리 결과 반환
        return dslRepository.readTeamList(dto);
    }

    /**
     * updateTeam : Team 수정
     * @param dto
     * @return
     * @throws Exception
     */
    @Transactional
    public void updateTeam(Long id, TeamRequestDto.UPDATE dto) throws NoSuchElementException, Exception {
        // 1. Team 조회
        Optional<Team> selectedTeam = repository.findById(id);

        // 2. 부재 시 예외 처리
        if (selectedTeam.isEmpty()) {
            throw new NoSuchElementException("수정하려는 팀이 존재하지 않습니다. ID : " + id);
        }

        // 3. 존재 시 수정 처리
        Team team = selectedTeam.get();
        team.updateTeam(dto.getName(), dto.getDescription());
    }

    /**
     * deleteTeam : Team 삭제
     * @param id
     * @throws Exception
     */
    @Transactional
    public void deleteTeam(Long id) throws NoSuchElementException, Exception {
        // 1. Team 조회
        Optional<Team> selectedTeam = repository.findById(id);

        // 2. 부재 시 예외 처리
        if (selectedTeam.isEmpty()) {
            throw new NoSuchElementException("삭제하려는 팀이 존재하지 않습니다. ID : " + id);
        }

        // 3. Team 추출
        Team team = selectedTeam.get();

        // 4. 존재 시 삭제 처리
        memberTeamService.deleteMemberTeamByTeamId(team);
        repository.delete(team);
    }

    // createMemberFromTeam

    @Transactional
    public void deleteMemberFromTeam(String email, Long teamId) throws NoSuchElementException, Exception {
        // 1. Team 조회
        Optional<Team> selectedTeam = repository.findById(teamId);

        // 2. 부재 시 예외 처리
        if (selectedTeam.isEmpty()) {
            throw new NoSuchElementException("수정하려는 팀이 존재하지 않습니다. ID : " + teamId);
        }

        // 3. Member 조회
        Optional<Member> selectedMember = memberRepository.findById(email);

        // 4. 부재 시 예외 처리
        if (selectedMember.isEmpty()) {
            throw new NoSuchElementException("제외하려는 멤버가 존재하지 않습니다. Email : " + email);
        }

        // 5. 객체 추출
        Member member = selectedMember.get();
        Team team = selectedTeam.get();

        // 6. DTO 생성
        MemberRequestDto.UPDATE memberDto = MemberRequestDto.UPDATE.builder()
                .email(email)
                .build();

        // 7. Member 수정
        memberService.updateMember(memberDto);

        // 8. MemberTeam 삭제
        memberTeamService.deleteMemberTeam(member, team);
    }
}