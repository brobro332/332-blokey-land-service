package xyz.samsami.blokey_land.member.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.repository.BlokeyRepository;
import xyz.samsami.blokey_land.common.ContainerBaseTest;
import xyz.samsami.blokey_land.member.domain.Member;
import xyz.samsami.blokey_land.member.dto.MemberRespDto;
import xyz.samsami.blokey_land.member.type.RoleType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.repository.ProjectRepository;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest extends ContainerBaseTest {
    @Autowired private MemberRepository repository;
    @Autowired private BlokeyRepository blokeyRepository;
    @Autowired private ProjectRepository projectRepository;

    UUID blokeyId1;
    UUID blokeyId2;
    Blokey blokey1;
    Blokey blokey2;
    String nickname = "테스트_닉네임_텍스트";
    String bio = "테스트_소개_텍스트";

    Project project;

    @BeforeEach
    void setUp() {
        blokeyId1 = UUID.randomUUID();
        blokeyId2 = UUID.randomUUID();

        blokey1 = blokeyRepository.save(
            Blokey.builder()
                .id(blokeyId1)
                .nickname(nickname + "_1")
                .bio(bio + "_1")
                .build()
        );
        blokey2 = blokeyRepository.save(
            Blokey.builder()
                .id(blokeyId2)
                .nickname(nickname + "_2")
                .bio(bio + "_2")
                .build()
        );

        project = projectRepository.save(
            Project.builder()
                .title("테스트_프로젝트_제목_텍스트")
                .description("테스트_프로젝트_설명_텍스트")
                .imageUrl("테스트_프로젝트_이미지_URL_텍스트")
                .isPrivate(true)
                .estimatedStartDate(LocalDate.now())
                .estimatedEndDate(LocalDate.now())
                .actualStartDate(LocalDate.now())
                .actualEndDate(LocalDate.now())
                .build()
        );

        repository.save(Member.builder().role(RoleType.LEADER).project(project).blokey(blokey1).build());
        repository.save(Member.builder().role(RoleType.LEADER).project(project).blokey(blokey2).build());
    }

    @Test
    @DisplayName("프로젝트 ID로 멤버 조회 시 해당 프로젝트의 멤버 목록이 페이지 형태로 반환된다")
    void givenProjectId_whenFindDtoByProjectId_thenReturnsPagedMembers() {
        // when
        Page<MemberRespDto> result = repository.findDtoByProjectId(project.getId(), PageRequest.of(0, 10));

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).extracting("nickname")
            .containsExactlyInAnyOrder(nickname + "_1", nickname + "_2");
    }

    @Test
    @DisplayName("사용자 ID로 멤버 조회 시 해당 사용자가 속한 멤버 목록이 페이지 형태로 반환된다")
    void givenBlokeyId_whenFindDtoByBlokeyId_thenReturnsPagedMembers() {
        // when
        Page<MemberRespDto> result = repository.findDtoByBlokeyId(blokey1.getId(), PageRequest.of(0, 10));

        // then
        assertThat(result)
            .isNotEmpty()
            .hasSize(1);
        MemberRespDto dto = result.getContent().getFirst();
        assertThat(dto.getNickname()).isEqualTo(nickname + "_1");
        assertThat(dto.getProjectId()).isEqualTo(project.getId());
    }
}