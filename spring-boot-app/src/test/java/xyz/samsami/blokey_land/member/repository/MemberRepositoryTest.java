package xyz.samsami.blokey_land.member.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class MemberRepositoryTest extends ContainerBaseTest {
    @Autowired private MemberRepository repository;
    @Autowired private BlokeyRepository blokeyRepository;
    @Autowired private ProjectRepository projectRepository;

    private Project project;
    private Blokey blokey1;

    @BeforeEach
    void setUp() {
        project = projectRepository.save(Project.builder().title("테스트").build());
        blokey1 = blokeyRepository.save(new Blokey(UUID.randomUUID(), "닉네임 1", "소개 1"));
        Blokey blokey2 = blokeyRepository.save(new Blokey(UUID.randomUUID(), "닉네임 2", "소개 2"));

        repository.save(Member.builder().role(RoleType.LEADER).project(project).blokey(blokey1).build());
        repository.save(Member.builder().role(RoleType.LEADER).project(project).blokey(blokey2).build());
    }

    @Test
    @DisplayName("Project ID로 Member 조회 시 해당 Project의 Member 목록이 페이지 형태로 반환된다")
    void givenProjectId_whenFindDtoByProjectId_thenReturnsPagedMembers() {
        // when
        Page<MemberRespDto> result = repository.findDtoByProjectId(project.getId(), PageRequest.of(0, 10));

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).extracting("nickname")
                .containsExactlyInAnyOrder("닉네임 1", "닉네임 2");
    }

    @Test
    @DisplayName("Blokey ID로 Member 조회 시 해당 Blokey가 속한 Member 목록이 페이지 형태로 반환된다")
    void givenBlokeyId_whenFindDtoByBlokeyId_thenReturnsPagedMembers() {
        // when
        Page<MemberRespDto> result = repository.findDtoByBlokeyId(blokey1.getId(), PageRequest.of(0, 10));

        // then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        MemberRespDto dto = result.getContent().getFirst();
        assertThat(dto.getNickname()).isEqualTo("닉네임 1");
        assertThat(dto.getProjectId()).isEqualTo(project.getId());
    }
}