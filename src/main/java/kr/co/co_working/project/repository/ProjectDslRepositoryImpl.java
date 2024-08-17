package kr.co.co_working.project.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.co_working.project.dto.ProjectRequestDto;
import kr.co.co_working.project.dto.ProjectResponseDto;
import kr.co.co_working.project.dto.QProjectResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.co_working.project.repository.entity.QProject.project;

@Repository
@RequiredArgsConstructor
public class ProjectDslRepositoryImpl implements ProjectDslRepository {
    private final JPAQueryFactory factory;

    /**
     * SELECT project_id, project_name, project_description, project_createdAt, project_modifiedAt
     * FROM tbl_project
     * WHERE project_name LIKE ?
     * AND team_id = ?;
     *
     * @param dto
     * @return
     */
    @Override
    public List<ProjectResponseDto> readProjectList(ProjectRequestDto.READ dto) {
        return factory
                .select(new QProjectResponseDto(project.id, project.name, project.description, project.createdAt, project.modifiedAt))
                .from(project)
                .where(nameContains(dto.getName()))
                .where(teamIdEq(dto.getTeamId()))
                .fetch();
    }

    private BooleanExpression nameContains(String nameCond) {
        return nameCond != null ? project.name.contains(nameCond) : null;
    }

    private BooleanExpression teamIdEq(Long teamIdCond) {
        return teamIdCond != null ? project.team.id.eq(teamIdCond) : null;
    }
}
