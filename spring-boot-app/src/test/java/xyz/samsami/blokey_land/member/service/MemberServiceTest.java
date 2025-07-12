package xyz.samsami.blokey_land.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.member.domain.Member;
import xyz.samsami.blokey_land.member.dto.MemberReqUpdateDto;
import xyz.samsami.blokey_land.member.mapper.MemberMapper;
import xyz.samsami.blokey_land.member.repository.MemberRepository;
import xyz.samsami.blokey_land.member.type.RoleType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.type.ProjectStatusType;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @InjectMocks private MemberService service;
    @Mock private MemberRepository repository;

    private Blokey blokey;
    private Project project;

    @BeforeEach
    void setUp() {
        UUID blokeyId = UUID.randomUUID();
        blokey = new Blokey(blokeyId, "nickname", "bio");

        Long projectId = 1L;
        project = Project.builder()
            .id(projectId)
            .title("제목")
            .description("설명")
            .status(ProjectStatusType.ACTIVE)
            .isPrivate(true)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now())
            .actualStartDate(LocalDate.now())
            .actualEndDate(LocalDate.now())
            .build();
    }

    @DisplayName("Member를 저장할 때 모든 필수 값이 저장되어야 한다.")
    @Test
    void givenValidParameter_whenCreateMember_thenAllFieldsShouldBeSaved() {
        // given
        Member member = mock(Member.class);
        try (MockedStatic<MemberMapper> mocked = mockStatic(MemberMapper.class)) {
            mocked.when(() -> MemberMapper.toEntity(project, blokey, RoleType.MEMBER))
                .thenReturn(member);

            // when
            service.createMember(project, blokey, RoleType.MEMBER);

            // then
            verify(repository).save(member);
        }
    }

    @Test
    @DisplayName("존재하는 ID가 주어졌을 때 Member가 반환되어야 한다.")
    void givenValidMemberId_whenFindMemberByMemberId_thenReturnMember() {
        // given
        Member member = mock(Member.class);
        Long memberId = 1L;

        when(repository.findById(memberId)).thenReturn(Optional.of(member));

        // when
        Member found = service.findMemberByMemberId(memberId);

        // then
        assertEquals(member, found);
    }

    @Test
    @DisplayName("존재하지 않는 ID가 주어졌을 때 예외가 발생해야 한다.")
    void givenInvalidMemberId_whenFindMemberByMemberId_thenThrowException() {
        // given
        Long memberId = 1L;
        when(repository.findById(memberId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CommonException.class, () -> service.findMemberByMemberId(memberId));
    }

    @Test
    @DisplayName("유효한 파라미터가 주어지면 Member 역할이 수정되어야 한다.")
    void givenValidParameter_whenUpdateMemberByMemberId_thenRoleUpdated() {
        // given
        Member member = mock(Member.class);
        Long memberId = 1L;
        MemberReqUpdateDto dto = MemberReqUpdateDto.builder()
            .role(RoleType.MEMBER)
            .build();

        when(repository.findById(memberId)).thenReturn(Optional.of(member));

        // when
        service.updateMemberByMemberId(memberId, dto);

        // then
        verify(member).updateRole(RoleType.MEMBER);
    }

    @Test
    @DisplayName("유효한 멤버 ID가 주어지면 Member가 삭제되어야 한다.")
    void givenValidMemberId_whenDeleteMemberByMemberId_thenDeleteCalled() {
        // given
        Member member = mock(Member.class);
        Long memberId = 1L;

        when(repository.findById(memberId)).thenReturn(Optional.of(member));

        // when
        service.deleteMember(memberId);

        // then
        verify(repository).delete(member);
    }
}