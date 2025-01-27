package kr.co.co_working.team.service;

import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.service.MemberService;
import kr.co.co_working.memberTeam.MemberTeam;
import kr.co.co_working.memberTeam.repository.MemberTeamRepository;
import kr.co.co_working.team.Team;
import kr.co.co_working.team.dto.TeamRequestDto;
import kr.co.co_working.team.dto.TeamResponseDto;
import kr.co.co_working.team.repository.TeamRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
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

        TeamRequestDto.CREATE dto = getCreateTeamDto(memberDto);

        /* when */
        Long id = service.createTeam(dto);

        /* then */
        Team team = repository.findById(id).get();
        Assertions.assertEquals("팀명 1", team.getName());
        Assertions.assertEquals("팀 소개입니다.", team.getDescription());

        List<MemberTeam> memberTeam = memberTeamRepository.findByTeamId(id);
        Assertions.assertEquals(id, memberTeam.get(0).getTeam().getId());
        Assertions.assertEquals(memberDto.getEmail(), memberTeam.get(0).getMember().getEmail());
    }



    @Test
    public void readTeam() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        TeamRequestDto.CREATE createDto = getCreateTeamDto(memberDto);
        service.createTeam(createDto);

        TeamRequestDto.READ readDto = new TeamRequestDto.READ();
        readDto.setEmail("test@korea.kr");

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

        TeamRequestDto.CREATE createDto = getCreateTeamDto(memberDto);

        Long id = service.createTeam(createDto);

        TeamRequestDto.UPDATE updateDto = new TeamRequestDto.UPDATE();
        updateDto.setName("팀명 2");
        updateDto.setDescription("팀 소개 수정입니다.");
        updateDto.setEmail("test@korea.kr");

        /* when */
        service.updateTeam(id, updateDto);

        /* then */
        Team team = repository.findById(id).get();
        Assertions.assertEquals("팀명 2", team.getName());
        Assertions.assertEquals("팀 소개 수정입니다.", team.getDescription());
        Assertions.assertEquals("test@korea.kr", team.getLeader());
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

    @Test
    public void addMemberToTeam() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        String email = memberService.createMember(memberDto);

        TeamRequestDto.CREATE teamDto = getCreateTeamDto(memberDto);
        Long teamId = service.createTeam(teamDto);

        /* when */
        service.addMemberToTeam(teamId, email);

        /* then */
        List<MemberTeam> memberTeamList = memberTeamRepository.findByTeamId(teamId);
        Assertions.assertEquals(2, memberTeamList.size());
    }

    @Test
    public void removeMemberToTeam() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        String email = memberService.createMember(memberDto);

        TeamRequestDto.CREATE teamDto = getCreateTeamDto(memberDto);
        Long teamId = service.createTeam(teamDto);

        /* when */
        MemberRequestDto.CREATE memberDtoToAdd = getCreateMemberDto();
        memberDtoToAdd.setEmail("other@korea.kr");
        String emailToAdd = memberService.createMember(memberDtoToAdd);

        service.addMemberToTeam(teamId, emailToAdd);
        service.removeMemberFromTeam(emailToAdd, teamId);

        /* then */
        List<MemberTeam> memberTeam2 = memberTeamRepository.findByTeamId(teamId);
        Assertions.assertEquals(1, memberTeam2.size());
    }

    /**
     * getCreateDto : Member CREATE DTO 반환
     * @return
     */
    private static MemberRequestDto.CREATE getCreateMemberDto() {
        MemberRequestDto.CREATE memberDto = new MemberRequestDto.CREATE();
        memberDto.setEmail("test@korea.kr");
        memberDto.setPassword("1234");
        memberDto.setName("김아무개");
        memberDto.setDescription("test");
        return memberDto;
    }

    /**
     * getCreateTeamDto : Team CREATE DTO 반환
     * @param memberDto
     * @return
     */
    private static TeamRequestDto.CREATE getCreateTeamDto(MemberRequestDto.CREATE memberDto) {
        TeamRequestDto.CREATE dto = new TeamRequestDto.CREATE();
        dto.setName("팀명 1");
        dto.setDescription("팀 소개입니다.");
        dto.setEmail(memberDto.getEmail());
        return dto;
    }
}