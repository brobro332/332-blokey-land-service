package kr.co.co_working.member.service;

import ch.qos.logback.core.util.StringUtil;
import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.repository.MemberRepository;
import kr.co.co_working.member.repository.entity.Member;
import kr.co.co_working.team.repository.TeamRepository;
import kr.co.co_working.team.repository.entity.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository repository;
    private final TeamRepository teamRepository;

    /**
     * createMember : Member 등록
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    public String createMember(MemberRequestDto.CREATE dto) throws NoSuchElementException, Exception {
        // 1. ID에 해당하는 Team 조회
        Optional<Team> selectedTeam = teamRepository.findById(dto.getTeamId());

        // 2. 부재 시 예외 처리
        if (selectedTeam.isEmpty()) {
            throw new NoSuchElementException("등록하려는 팀이 존재하지 않습니다. ID : " + dto.getTeamId());
        }

        // 3. Member 빌드
        Member member = Member.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .name(dto.getName())
                .description(StringUtil.nullStringToEmpty(dto.getDescription()))
                .team(selectedTeam.get())
                .build();

        // 4. Member 등록
        repository.save(member);

        // 5. Email 반환
        return member.getEmail();
    }
}
