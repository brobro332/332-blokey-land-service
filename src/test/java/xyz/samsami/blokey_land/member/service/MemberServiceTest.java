package xyz.samsami.blokey_land.member.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import xyz.samsami.blokey_land.member.domain.Member;
import xyz.samsami.blokey_land.member.dto.MemberReqCreateDto;
import xyz.samsami.blokey_land.member.dto.MemberReqDeleteDto;
import xyz.samsami.blokey_land.member.dto.MemberReqUpdateDto;
import xyz.samsami.blokey_land.member.dto.MemberRespDto;
import xyz.samsami.blokey_land.member.repository.MemberRepository;
import xyz.samsami.blokey_land.member.type.RoleType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.service.ProjectService;
import xyz.samsami.blokey_land.user.domain.User;
import xyz.samsami.blokey_land.user.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @InjectMocks
    private MemberService service;

    @Mock
    private ProjectService projectService;

    @Mock
    private UserService userService;

    @Mock
    private MemberRepository repository;

    @Test
    void createMember_validInput_true() {
        // given
        Long projectId = 1L;
        UUID userId = UUID.randomUUID();
        Project project = Project.builder()
            .id(1L)
            .title("BLOKEY-LAND PROJECT")
            .description("GitHub 기반 프로젝트 관리 및 팀원 매칭 서비스입니다.")
            .ownerId(userId)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now().plusDays(2))
            .actualStartDate(LocalDate.now().plusDays(3))
            .actualEndDate(LocalDate.now().plusDays(4))
            .build();
        User user = new User(userId, "짱구", "감자머리입니다.");

        MemberReqCreateDto dto = MemberReqCreateDto.builder()
            .userId(userId)
            .build();

        when(projectService.findProjectByProjectId(projectId)).thenReturn(project);
        when(userService.findUserByUserId(userId)).thenReturn(user);

        // when
        service.createMember(projectId, dto);

        // then
        verify(repository).save(any(Member.class));
    }

    @Test
    void createMember_invalidInput_false() {
        Long projectId = 1L;
        UUID userId = UUID.randomUUID();

        MemberReqCreateDto dto = MemberReqCreateDto.builder()
            .userId(userId)
            .build();

        when(projectService.findProjectByProjectId(projectId)).thenReturn(null);
        when(userService.findUserByUserId(userId)).thenReturn(null);

        service.createMember(projectId, dto);

        verify(repository, never()).save(any());
    }

    @Test
    void readMemberByProjectId_validInput_true() {
        // given
        Long projectId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<MemberRespDto> list = List.of(
            new MemberRespDto(1L, RoleType.MEMBER, projectId, UUID.randomUUID())
        );
        Page<MemberRespDto> page = new PageImpl<>(list, pageable, list.size());

        when(repository.findDtoByProjectId(projectId, pageable)).thenReturn(page);

        // when
        Page<MemberRespDto> result = service.readMemberByProjectId(projectId, pageable);

        // then
        assertEquals(1, result.getContent().size());
        assertEquals(projectId, result.getContent().getFirst().getProjectId());
        verify(repository).findDtoByProjectId(projectId, pageable);
    }

    @Test
    void readMemberByUserId_validInput_true() {
        // given
        UUID userId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        List<MemberRespDto> list = List.of(
            new MemberRespDto(2L, RoleType.LEADER, 1L, userId)
        );
        Page<MemberRespDto> expectedPage = new PageImpl<>(list, pageable, list.size());

        when(repository.findDtoByUserId(userId, pageable)).thenReturn(expectedPage);

        // when
        Page<MemberRespDto> result = service.readMemberByUserId(userId, pageable);

        // then
        assertEquals(1, result.getContent().size());
        assertEquals(userId, result.getContent().getFirst().getUserId());
        verify(repository).findDtoByUserId(userId, pageable);
    }

    @Test
    void updateMemberByMemberId_validInput_true() {
        // given
        Long memberId = 1L;
        RoleType role = RoleType.LEADER;
        Member member = mock(Member.class);
        MemberReqUpdateDto dto = MemberReqUpdateDto.builder()
            .role(role)
            .build();

        MemberService spyService = spy(service);
        doReturn(member).when(spyService).findMemberByMemberId(memberId);

        // when
        spyService.updateMemberByMemberId(memberId, dto);

        // then
        verify(member).updateRole(role);
    }

    @Test
    void deleteMember_validInput_true() {
        // given
        UUID userId = UUID.randomUUID();
        MemberReqDeleteDto dto = MemberReqDeleteDto.builder()
            .userId(userId)
            .build();
        Member member = mock(Member.class);
        Project project = Project.builder()
            .id(1L)
            .title("BLOKEY-LAND PROJECT")
            .description("GitHub 기반 프로젝트 관리 및 팀원 매칭 서비스입니다.")
            .ownerId(userId)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now().plusDays(2))
            .actualStartDate(LocalDate.now().plusDays(3))
            .actualEndDate(LocalDate.now().plusDays(4))
            .build();
        User user = new User(userId, "짱구", "감자머리입니다.");

        when(projectService.findProjectByProjectId(1L)).thenReturn(project);
        when(userService.findUserByUserId(userId)).thenReturn(user);

        MemberService spyService = spy(service);
        doReturn(member).when(spyService).findMemberByProjectAndUser(project, user);

        // when
        spyService.deleteMember(1L, dto);

        // then
        verify(repository).delete(member);
    }
}