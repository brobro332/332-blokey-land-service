package kr.co.co_working.team.service;

import kr.co.co_working.team.dto.TeamRequestDto;
import kr.co.co_working.team.dto.TeamResponseDto;
import kr.co.co_working.team.repository.TeamRepository;
import kr.co.co_working.team.repository.entity.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TeamServiceTest {
    @Autowired
    TeamService service;

    @Autowired
    TeamRepository repository;

    @Test
    public void createTeam() throws Exception {
        /* given */
        TeamRequestDto.CREATE dto = new TeamRequestDto.CREATE();
        dto.setName("팀명 1");
        dto.setDescription("팀 소개입니다.");

        /* when */
        Long id = service.createTeam(dto);

        /* then */
        Team team = repository.findById(id).get();
        Assertions.assertEquals("팀명 1", team.getName());
        Assertions.assertEquals("팀 소개입니다.", team.getDescription());
    }
    
    @Test
    public void readTeam() throws Exception {
        /* given */
        TeamRequestDto.CREATE createDto = new TeamRequestDto.CREATE();
        createDto.setName("팀명 1");
        createDto.setDescription("팀 소개입니다.");

        service.createTeam(createDto);

        TeamRequestDto.READ readDto = new TeamRequestDto.READ();
        readDto.setName("명");

        /* when */
        List<TeamResponseDto> teams = service.readTeam(readDto);
        
        /* then */
        Assertions.assertEquals(1, teams.size());
        Assertions.assertEquals("팀명 1", teams.get(0).getName());
        Assertions.assertEquals("팀 소개입니다.", teams.get(0).getDescription());
    }

    @Test
    public void updateTeam() throws Exception {
        /* given */
        TeamRequestDto.CREATE createDto = new TeamRequestDto.CREATE();
        createDto.setName("팀명 1");
        createDto.setDescription("팀 소개입니다.");

        Long id = service.createTeam(createDto);

        TeamRequestDto.UPDATE updateDto = new TeamRequestDto.UPDATE();
        updateDto.setName("팀명 2");
        updateDto.setDescription("팀 소개 수정입니다.");

        /* when */
        service.updateTeam(id, updateDto);

        /* then */
        Team team = repository.findById(id).get();
        Assertions.assertEquals("팀명 2", team.getName());
        Assertions.assertEquals("팀 소개 수정입니다.", team.getDescription());
    }

    @Test
    public void deleteTeam() throws Exception {
        /* given */
        List<Long> idList = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            TeamRequestDto.CREATE createDto = new TeamRequestDto.CREATE();
            createDto.setName("팀명 " + i);
            createDto.setDescription("팀 소개입니다. " + i);
            idList.add(service.createTeam(createDto));
            createDto = null;
        }

        /* when */
        Team team = repository.findById(idList.get(0)).get();
        repository.delete(team);

        /* then */
        List<Team> teams = repository.findAll();

        Assertions.assertEquals(1, teams.size());
        Assertions.assertEquals("팀명 2", teams.get(0).getName());
        Assertions.assertEquals("팀 소개입니다. 2", teams.get(0).getDescription());
    }
}