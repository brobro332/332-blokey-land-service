package xyz.samsami.blokey_land.project.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.repository.BlokeyRepository;
import xyz.samsami.blokey_land.common.ContainerBaseTest;
import xyz.samsami.blokey_land.member.domain.Member;
import xyz.samsami.blokey_land.member.repository.MemberRepository;
import xyz.samsami.blokey_land.member.type.RoleType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.dto.ProjectRespDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProjectRepositoryTest extends ContainerBaseTest {
    @Autowired private ProjectRepository repository;
    @Autowired private BlokeyRepository blokeyRepository;
    @Autowired private MemberRepository memberRepository;

    @Test
    @DisplayName("사용자 ID로 프로젝트 조회 시 해당 사용자의 프로젝트 목록과 해당 프로젝트 내 역할이 함께 반환된다")
    void givenProjectId_whenFindDtoByProjectId_thenReturnsPagedMembers() {
        // given
        UUID blokeyId = UUID.randomUUID();
        Blokey blokey =blokeyRepository.save(new Blokey(blokeyId, "닉네임", "소개"));

        Project project1 = repository.save(Project.builder()
            .title("제목 1")
            .description("설명 1")
            .imageUrl("이미지 URL 1")
            .isPrivate(true)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now())
            .actualStartDate(LocalDate.now())
            .actualEndDate(LocalDate.now())
            .build());
        Project project2 = repository.save(Project.builder()
                .title("제목 2")
                .description("설명 2")
                .imageUrl("이미지 URL 2")
                .isPrivate(true)
                .estimatedStartDate(LocalDate.now())
                .estimatedEndDate(LocalDate.now())
                .actualStartDate(LocalDate.now())
                .actualEndDate(LocalDate.now())
                .build());

        memberRepository.save(Member.builder().role(RoleType.LEADER).project(project1).blokey(blokey).build());
        memberRepository.save(Member.builder().role(RoleType.LEADER).project(project2).blokey(blokey).build());

        // when
        List<ProjectRespDto> result = repository.findProjectsWithRoleByBlokeyId(blokeyId);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result).extracting("title")
            .containsExactlyInAnyOrder("제목 1", "제목 2");
    }
}