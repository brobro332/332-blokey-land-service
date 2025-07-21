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
    @InjectMocks MemberService service;
    @Mock MemberRepository repository;

    Blokey blokey;
    String nickname = "테스트_닉네임_텍스트";
    String bio = "테스트_소개_텍스트";

    Project project;
    String title = "테스트_프로젝트_제목_텍스트";
    String description = "테스트_프로젝트_설명_텍스트";
    String imageUrl = "테스트_프로젝트_이미지_URL_텍스트";

    Long memberId;
    Member member;

    @BeforeEach
    void setUp() {
        UUID blokeyId = UUID.randomUUID();
        blokey = Blokey.builder()
            .id(blokeyId)
            .nickname(nickname)
            .bio(bio)
            .build();

        Long projectId = 1L;
        project = Project.builder()
            .id(projectId)
            .title(title)
            .description(description)
            .imageUrl(imageUrl)
            .status(ProjectStatusType.ACTIVE)
            .isPrivate(true)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now())
            .actualStartDate(LocalDate.now())
            .actualEndDate(LocalDate.now())
            .build();

        memberId = 1L;
        member = mock(Member.class);
    }

    @DisplayName("유효한 파라미터가 주어진다면_멤버를 저장할 때_모든 필수 값이 저장되어야 한다.")
    @Test
    void givenValidParameter_whenCreateMember_thenAllFieldsShouldBeSaved() {
        // given
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
    @DisplayName("존재하는 ID가 주어진다면_멤버를 조회할 때_결과가 반환되어야 한다.")
    void givenExistingId_whenFindMemberByMemberId_thenReturnMember() {
        // given
        when(repository.findById(memberId)).thenReturn(Optional.of(member));

        // when
        Member result = service.findMemberByMemberId(memberId);

        // then
        assertEquals(member, result);
    }

    @Test
    @DisplayName("존재하지 않는 ID가 주어진다면_멤버를 조회할 때_예외가 발생해야 한다.")
    void givenNonExistingId_whenFindMemberByMemberId_thenThrowsException() {
        // given
        when(repository.findById(memberId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CommonException.class, () -> service.findMemberByMemberId(memberId));
    }

    @Test
    @DisplayName("유효한 파라미터가 주어진다면_멤버를 수정할 때_올바르게 갱신되어야 한다.")
    void givenValidParameter_whenUpdateMemberByMemberId_thenRoleUpdated() {
        // given
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
    @DisplayName("유효한 ID가 주어진다면_멤버를 삭제할 때_올바르게 삭제되어야 한다.")
    void givenValidId_whenDeleteMemberByMemberId_thenDeleteCalled() {
        // given
        when(repository.findById(memberId)).thenReturn(Optional.of(member));

        // when
        service.deleteMember(memberId);

        // then
        verify(repository).delete(member);
    }
}