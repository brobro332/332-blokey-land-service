package kr.co.co_working.team.service;

import kr.co.co_working.team.dto.TeamRequestDto;
import kr.co.co_working.team.dto.TeamResponseDto;
import kr.co.co_working.team.repository.TeamDslRepository;
import kr.co.co_working.team.repository.TeamRepository;
import kr.co.co_working.team.repository.entity.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository repository;
    private final TeamDslRepository dslRepository;

    /**
     * createTeam : Team 등록
     * @param dto
     * @return
     * @throws Exception
     */
    public Long createTeam(TeamRequestDto.CREATE dto) throws Exception {
        // 1. Team 빌드
        Team team = Team.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        // 2. Team 등록
        repository.save(team);

        // 3. ID 반환
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
        
        // 3. 존재 시 삭제 처리
        repository.delete(selectedTeam.get());
    }
}
