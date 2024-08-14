package kr.co.co_working.project.service;

import kr.co.co_working.project.dto.ProjectRequestDto;
import kr.co.co_working.project.dto.ProjectResponseDto;
import kr.co.co_working.project.repository.ProjectDslRepository;
import kr.co.co_working.project.repository.ProjectRepository;
import kr.co.co_working.project.repository.entity.Project;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class ProjectServiceTest {
    @Autowired
    ProjectService service;

    @Autowired
    ProjectRepository repository;

    @Autowired
    ProjectDslRepository dslRepository;

    @Test
    public void createProject() throws Exception {
        /* given */
        ProjectRequestDto.CREATE dto = new ProjectRequestDto.CREATE();
        dto.setName("프로젝트 A");
        dto.setDescription("프로젝트 관리 프로그램 만들기");
        dto.setTasks(new ArrayList<>());

        /* when */
        Long id = service.createProject(dto);

        /* then */
        Project project = repository.findById(id).get();
        Assertions.assertEquals("프로젝트 A", project.getName());
        Assertions.assertEquals("프로젝트 관리 프로그램 만들기", project.getDescription());
        Assertions.assertEquals(0, project.getTasks().size());
    }

    @Test
    public void readProjectList() throws Exception {
        /* given */
        ProjectRequestDto.CREATE dto = new ProjectRequestDto.CREATE();
        dto.setName("프로젝트 A");
        dto.setDescription("프로젝트 관리 프로그램 만들기");
        dto.setTasks(new ArrayList<>());
        service.createProject(dto);

        /* when */
        List<ProjectResponseDto> projects = dslRepository.readProjectList(new ProjectRequestDto.READ("젝트"));

        /* then */
        Assertions.assertEquals(1, projects.size());
    }

    @Test
    public void updateProject() throws Exception {
        /* given */
        ProjectRequestDto.CREATE dto = new ProjectRequestDto.CREATE();
        dto.setName("프로젝트 A");
        dto.setDescription("프로젝트 관리 프로그램 만들기");
        dto.setTasks(new ArrayList<>());
        Long id = service.createProject(dto);

        /* when */
        service.updateProject(id, new ProjectRequestDto.UPDATE("프로젝트 B", "명세 수정", new ArrayList<>()));

        /* then */
        Project project = repository.findById(id).get();
        Assertions.assertEquals("프로젝트 B", project.getName());
        Assertions.assertEquals("명세 수정", project.getDescription());
        Assertions.assertEquals(0, project.getTasks().size());
    }

    @Test
    public void deleteProject() throws Exception {
        /* given */
        List<Long> idList = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            ProjectRequestDto.CREATE dto = new ProjectRequestDto.CREATE();
            dto.setName("프로젝트 " + i);
            dto.setDescription("프로젝트 관리 프로그램 만들기 " + i);
            dto.setTasks(new ArrayList<>());
            idList.add(service.createProject(dto));
            dto = null;
        }

        /* when */
        repository.delete(repository.findById(idList.get(0)).get());

        /* then */
        Project project = repository.findById(idList.get(1)).get();
        Assertions.assertEquals("프로젝트 2", project.getName());
        Assertions.assertEquals("프로젝트 관리 프로그램 만들기 2", project.getDescription());
        Assertions.assertEquals(1, repository.findAll().size());
    }
}