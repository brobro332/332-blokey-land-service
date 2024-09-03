package kr.co.co_working.team.service;

import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.service.MemberService;
import kr.co.co_working.memberTeam.MemberTeam;
import kr.co.co_working.memberTeam.repository.MemberTeamRepository;
import kr.co.co_working.team.dto.TeamRequestDto;
import kr.co.co_working.team.dto.TeamResponseDto;
import kr.co.co_working.team.repository.TeamRepository;
import kr.co.co_working.team.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class TeamServiceTest {
    @Autowired
    TeamService service;

    @Autowired
    MemberService memberService;

    @Autowired
    TeamRepository repository;

    @Autowired
    MemberTeamRepository memberTeamRepository;

    @Test
    public void createTeam() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        TeamRequestDto.CREATE dto = getCreateMemberDto(memberDto);

        /* when */
        Long id = service.createTeam(dto);

        /* then */
        Team team = repository.findById(id).get();
        Assertions.assertEquals("팀명 1", team.getName());
        Assertions.assertEquals("팀 소개입니다.", team.getDescription());

        MemberTeam memberTeam = memberTeamRepository.findByTeamId(id);
        Assertions.assertEquals(id, memberTeam.getTeam().getId());
        Assertions.assertEquals(memberDto.getEmail(), memberTeam.getMember().getEmail());
    }



    @Test
    public void readTeam() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        TeamRequestDto.CREATE createDto = getCreateMemberDto(memberDto);
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
        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        TeamRequestDto.CREATE createDto = getCreateMemberDto(memberDto);

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
        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        List<Long> idList = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            TeamRequestDto.CREATE createDto = new TeamRequestDto.CREATE();
            createDto.setName("팀명 " + i);
            createDto.setDescription("팀 소개입니다. " + i);
            createDto.setEmail(memberDto.getEmail());
            idList.add(service.createTeam(createDto));
            createDto = null;
        }

        /* when */
        Team team = repository.findById(idList.get(0)).get();
        service.deleteTeam(team.getId());

        /* then */
        List<Team> teams = repository.findAll();

        Assertions.assertEquals(1, teams.size());
        Assertions.assertEquals("팀명 2", teams.get(0).getName());
        Assertions.assertEquals("팀 소개입니다. 2", teams.get(0).getDescription());
    }

    private static MemberRequestDto.CREATE getCreateMemberDto() {
        MemberRequestDto.CREATE memberDto = new MemberRequestDto.CREATE();
        memberDto.setEmail("test@korea.kr");
        memberDto.setPassword("1234");
        memberDto.setName("김아무개");
        memberDto.setDescription("test");
        return memberDto;
    }

    private static TeamRequestDto.CREATE getCreateMemberDto(MemberRequestDto.CREATE memberDto) {
        TeamRequestDto.CREATE dto = new TeamRequestDto.CREATE();
        dto.setName("팀명 1");
        dto.setDescription("팀 소개입니다.");
        dto.setEmail(memberDto.getEmail());
        return dto;
    }
}