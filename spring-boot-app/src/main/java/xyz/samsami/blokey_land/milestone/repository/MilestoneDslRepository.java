package xyz.samsami.blokey_land.milestone.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xyz.samsami.blokey_land.milestone.domain.Milestone;
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqReadDto;
import xyz.samsami.blokey_land.milestone.dto.MilestoneRespDto;
import xyz.samsami.blokey_land.milestone.mapper.MilestoneMapper;

import java.util.List;

import static xyz.samsami.blokey_land.milestone.domain.QMilestone.milestone;
import static xyz.samsami.blokey_land.project.domain.QProject.project;

@Repository
@RequiredArgsConstructor
public class MilestoneDslRepository {
    private final JPAQueryFactory queryFactory;

    public List<MilestoneRespDto> readMilestones(MilestoneReqReadDto dto) {
        List<Milestone> milestones = fetchMilestones(dto);

        return milestones.stream()
            .map(MilestoneMapper::toRespDto)
            .toList();
    }

    private BooleanBuilder buildPredicate(MilestoneReqReadDto dto) {
        BooleanBuilder builder = new BooleanBuilder();

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) builder.and(milestone.title.containsIgnoreCase(dto.getTitle()));
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) builder.and(milestone.description.containsIgnoreCase(dto.getDescription()));
        if (dto.getProjectId() != null) builder.and(milestone.project.id.eq(dto.getProjectId()));

        if (dto.getDueDateFrom() != null && dto.getDueDateTo() != null) {
            builder.and(milestone.dueDate.between(dto.getDueDateFrom(), dto.getDueDateTo()));
        } else if (dto.getDueDateFrom() != null) {
            builder.and(milestone.dueDate.goe(dto.getDueDateFrom()));
        } else if (dto.getDueDateTo() != null) {
            builder.and(milestone.dueDate.loe(dto.getDueDateTo()));
        }

        if (dto.getMonth() != null) {
            builder.and(
                Expressions.numberTemplate(Integer.class, "MONTH({0})", milestone.dueDate).eq(dto.getMonth())
            );
        }

        return builder;
    }

    private List<Milestone> fetchMilestones(MilestoneReqReadDto dto) {
        BooleanBuilder predicate = buildPredicate(dto);

        JPAQuery<Milestone> query = queryFactory
            .selectFrom(milestone)
            .join(milestone.project, project).fetchJoin()
            .where(predicate)
            .orderBy(milestone.id.desc());

        return query.fetch();
    }
}
