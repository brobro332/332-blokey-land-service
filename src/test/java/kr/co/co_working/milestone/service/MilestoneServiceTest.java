package kr.co.co_working.milestone.service;

import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.service.MemberService;
import kr.co.co_working.milestone.Milestone;
import kr.co.co_working.milestone.dto.MilestoneRequestDto;
import kr.co.co_working.milestone.dto.MilestoneResponseDto;
import kr.co.co_working.milestone.repository.MilestoneRepository;
import kr.co.co_working.project.dto.ProjectRequestDto;
import kr.co.co_working.project.service.ProjectService;
import kr.co.co_working.team.dto.TeamRequestDto;
import kr.co.co_working.team.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
class MilestoneServiceTest {
    @Autowired
    MilestoneService service;

    @Autowired
    MemberService memberService;

    @Autowired
    TeamService teamService;

    @Autowired
    ProjectService projectService;

    @Autowired
    MilestoneRepository repository;

    @Test
    public void createMilestone() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        TeamRequestDto.CREATE teamDto = getCreateTeamDto(memberDto);
        Long teamId = teamService.createTeam(teamDto);

        ProjectRequestDto.CREATE projectDto = getCreateProjectDto(teamId);
        Long projectId = projectService.createProject(projectDto);

        MilestoneRequestDto.CREATE milestoneDto = getCreateMilestoneDto(projectId);

        /* when */
        Long id = service.createMilestone(milestoneDto);

        /* then */
        Milestone milestone = repository.findById(id).get();

        Assertions.assertEquals(id, milestone.getId());
        Assertions.assertEquals("마일스톤 A", milestone.getName());
        Assertions.assertEquals("계획을 위한 마일스톤입니다.", milestone.getDescription());
        Assertions.assertEquals(
            LocalDateTime.of(
                2024,
                9,
                11,
                0,
                0,
                0
            ),
            milestone.getDueAt()
        );
    }

    @Test
    public void readMilestoneList() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        TeamRequestDto.CREATE teamDto = getCreateTeamDto(memberDto);
        Long teamId = teamService.createTeam(teamDto);

        ProjectRequestDto.CREATE projectDto = getCreateProjectDto(teamId);
        Long projectId = projectService.createProject(projectDto);

        MilestoneRequestDto.CREATE milestoneDto = getCreateMilestoneDto(projectId);
        service.createMilestone(milestoneDto);

        /* when */
        List<MilestoneResponseDto> milestones = service.readMilestone(
            new MilestoneRequestDto.READ(
                projectId,
                "일스",
                "위한",
                    LocalDateTime.of(
                        2024,
                        9,
                        4,
                        0,
                        0,
                        0
                ),
                LocalDateTime.of(
                        2024,
                        9,
                        12,
                        0,
                        0,
                        0
                )
            )
        );

        /* then */
        Assertions.assertEquals(1, milestones.size());
    }

    @Test
    public void updateMilestone() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        TeamRequestDto.CREATE teamDto = getCreateTeamDto(memberDto);
        Long teamId = teamService.createTeam(teamDto);

        ProjectRequestDto.CREATE projectDto = getCreateProjectDto(teamId);
        Long projectId = projectService.createProject(projectDto);

        MilestoneRequestDto.CREATE milestoneDto = getCreateMilestoneDto(projectId);
        Long id = service.createMilestone(milestoneDto);

        /* when */
        service.updateMilestone(
            new MilestoneRequestDto.UPDATE(
                id,
                projectId,
                "수정한 마일스톤명",
                "수정한 명세",
                LocalDateTime.of(
                        2024,
                        9,
                        12,
                        0,
                        0,
                        0
                )
            )
        );

        /* then */
        Milestone milestone = repository.findById(id).get();
        Assertions.assertEquals("수정한 마일스톤명", milestone.getName());
        Assertions.assertEquals("수정한 명세", milestone.getDescription());
        Assertions.assertEquals(
            LocalDateTime.of(
                2024,
                9,
                12,
                0,
                0,
                0
            ), milestone.getDueAt()
        );
    }
    
    /**
     * getCreateMemberDto : Member DTO 생성
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
     * getCreateTeamDto : Team DTO 생성
     * @param memberDto
     * @return
     */
    private static TeamRequestDto.CREATE getCreateTeamDto(MemberRequestDto.CREATE memberDto) {
        TeamRequestDto.CREATE teamDto = new TeamRequestDto.CREATE();
        teamDto.setName("팀명 1");
        teamDto.setDescription("팀 소개입니다.");
        teamDto.setEmail(memberDto.getEmail());
        return teamDto;
    }

    /**
     * getCreateProjectDto : Project DTO 생성
     * @param teamId
     * @return
     */
    private static ProjectRequestDto.CREATE getCreateProjectDto(Long teamId) {
        ProjectRequestDto.CREATE projectDto = new ProjectRequestDto.CREATE();
        projectDto.setName("프로젝트 A");
        projectDto.setDescription("프로젝트 관리 프로그램 만들기");
        projectDto.setTeamId(teamId);
        return projectDto;
    }

    /**
     * getCreateMilestoneDto : Milestone DTO 생성
     * @param projectId
     * @return
     */
    private static MilestoneRequestDto.CREATE getCreateMilestoneDto(Long projectId) {
        MilestoneRequestDto.CREATE milestoneDto = new MilestoneRequestDto.CREATE();
        milestoneDto.setName("마일스톤 A");
        milestoneDto.setDescription("계획을 위한 마일스톤입니다.");
        milestoneDto.setProjectId(projectId);
        milestoneDto.setDueAt(
                LocalDateTime.of(
                        2024,
                        9,
                        11,
                        0,
                        0,
                        0
                )
        );
        return milestoneDto;
    }
    
    @Test
    public void deleteMilestone() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        TeamRequestDto.CREATE teamDto = getCreateTeamDto(memberDto);
        Long teamId = teamService.createTeam(teamDto);

        ProjectRequestDto.CREATE projectDto = getCreateProjectDto(teamId);
        Long projectId = projectService.createProject(projectDto);

        MilestoneRequestDto.CREATE milestoneDto = getCreateMilestoneDto(projectId);
        Long id = service.createMilestone(milestoneDto);

        /* when */
        service.deleteMilestone(
            new MilestoneRequestDto.DELETE(
                id,
                projectId
            )
        );

        /* then */
        Assertions.assertEquals(0, repository.findAll().size());
    }
}