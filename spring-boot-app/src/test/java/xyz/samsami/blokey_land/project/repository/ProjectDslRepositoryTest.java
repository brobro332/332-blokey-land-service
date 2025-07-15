package xyz.samsami.blokey_land.project.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.repository.BlokeyRepository;
import xyz.samsami.blokey_land.common.ContainerBaseTest;
import xyz.samsami.blokey_land.member.domain.Member;
import xyz.samsami.blokey_land.member.repository.MemberRepository;
import xyz.samsami.blokey_land.member.type.RoleType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.dto.ProjectReqReadDto;
import xyz.samsami.blokey_land.project.dto.ProjectOnlyRespDto;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProjectDslRepositoryTest extends ContainerBaseTest {
    @Autowired private ProjectDslRepository dslRepository;
    @Autowired private ProjectRepository repository;
    @Autowired private BlokeyRepository blokeyRepository;
    @Autowired private MemberRepository memberRepository;

    @Test
    void givenTitleFilter_whenReadProjectsSlice_thenFiltered() {
        UUID blokeyId = UUID.randomUUID();
        Blokey blokey = blokeyRepository.save(new Blokey(blokeyId, "닉네임", "소개"));

        Project project1 = repository.save(Project.builder().title("검색어").build());
        Project project2 = repository.save(Project.builder().title("다른 제목").build());

        memberRepository.save(Member.builder().role(RoleType.LEADER).project(project1).blokey(blokey).build());
        memberRepository.save(Member.builder().role(RoleType.LEADER).project(project2).blokey(blokey).build());

        ProjectReqReadDto dto = new ProjectReqReadDto();
        dto.setTitle("검색어");

        Pageable pageable = PageRequest.of(0, 10);
        Slice<ProjectOnlyRespDto> result = dslRepository.readProjectsSlice(dto, blokey.getId().toString(), pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getTitle()).contains("검색어");
    }

    @Test
    void givenValidParameter_whenReadProjectsPage_thenReturnPage() {
        // given
        UUID blokeyId = UUID.randomUUID();
        Blokey blokey = blokeyRepository.save(new Blokey(blokeyId, "닉네임", "소개"));

        for (int i = 0; i < 15; i++) {
            Project project = repository.save(Project.builder()
                .title("테스트 " + i)
                .description("설명 " + i)
                .isPrivate(false)
                .build());

            memberRepository.save(Member.builder().role(RoleType.LEADER).project(project).blokey(blokey).build());
        }

        ProjectReqReadDto dto = new ProjectReqReadDto();
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<ProjectOnlyRespDto> result = dslRepository.readProjectsPage(dto, blokey.getId().toString(), pageable);

        // then
        assertThat(result.getContent()).hasSize(10);
        assertThat(result.getTotalElements()).isEqualTo(15);
        assertThat(result.getContent()).allMatch(ProjectOnlyRespDto::getIsLeader);
    }
}