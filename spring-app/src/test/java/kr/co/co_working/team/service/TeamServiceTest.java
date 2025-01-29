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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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

    @BeforeEach
    void setUp() {
        String email = "test@korea.kr";
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            email, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void createTeam() throws Exception {
        /* given */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

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
        Assertions.assertEquals(email, memberTeam.get(0).getMember().getEmail());
    }



    @Test
    public void readTeam() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        TeamRequestDto.CREATE createDto = getCreateTeamDto(memberDto);
        service.createTeam(createDto);

        TeamRequestDto.READ readDto = new TeamRequestDto.READ();

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        TeamRequestDto.CREATE teamDto = getCreateTeamDto(memberDto);
        Long teamId = service.createTeam(teamDto);

        /* when */
        service.removeMemberFromTeam(email, teamId);

        /* then */
        List<MemberTeam> memberTeam = memberTeamRepository.findByTeamId(teamId);
        Assertions.assertEquals(0, memberTeam.size());
    }

    /**
     * getCreateDto : Member CREATE DTO 반환
     * @return
     */
    private static MemberRequestDto.CREATE getCreateMemberDto() {
        MemberRequestDto.CREATE memberDto = new MemberRequestDto.CREATE();
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
        return dto;
    }
}