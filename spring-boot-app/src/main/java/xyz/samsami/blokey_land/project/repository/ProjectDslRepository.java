package xyz.samsami.blokey_land.project.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import xyz.samsami.blokey_land.member.type.RoleType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.dto.ProjectReqReadDto;
import xyz.samsami.blokey_land.project.dto.ProjectRespDto;
import xyz.samsami.blokey_land.project.mapper.ProjectMapper;

import java.util.List;
import java.util.UUID;

import static xyz.samsami.blokey_land.blokey.domain.QBlokey.blokey;
import static xyz.samsami.blokey_land.member.domain.QMember.member;
import static xyz.samsami.blokey_land.project.domain.QProject.project;

@Repository
@RequiredArgsConstructor
public class ProjectDslRepository {

    private final JPAQueryFactory queryFactory;

    public Slice<ProjectRespDto> readProjectsSlice(ProjectReqReadDto dto, String blokeyId, Pageable pageable) {
        UUID blokeyUuid = UUID.fromString(blokeyId);

        List<Tuple> rows = baseQuery(dto, blokeyUuid)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .orderBy(project.id.desc())
            .fetch();

        boolean hasNext = rows.size() > pageable.getPageSize();
        if (hasNext) rows.removeLast();

        List<ProjectRespDto> dtoList = rows.stream()
            .map(tuple -> {
                Project p = tuple.get(project);
                RoleType role = tuple.get(member.role);
                return ProjectMapper.toRespDtoWithIsLeader(p, role == RoleType.LEADER);
            })
            .toList();

        return new SliceImpl<>(dtoList, pageable, hasNext);
    }

    public Page<ProjectRespDto> readProjectsPage(ProjectReqReadDto dto, String blokeyId, Pageable pageable) {
        UUID blokeyUuid = UUID.fromString(blokeyId);

        List<Tuple> rows = baseQuery(dto, blokeyUuid)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(project.id.desc())
            .fetch();

        long totalCount = countQuery(dto, blokeyUuid);

        List<ProjectRespDto> dtoList = rows.stream()
            .map(tuple -> {
                Project p = tuple.get(project);
                RoleType role = tuple.get(member.role);
                return ProjectMapper.toRespDtoWithIsLeader(p, role == RoleType.LEADER);
            })
            .toList();

        return new PageImpl<>(dtoList, pageable, totalCount);
    }

    private JPAQuery<Tuple> baseQuery(ProjectReqReadDto dto, UUID blokeyUuid) {
        return queryFactory.select(project, member.role)
            .from(member)
            .join(member.project, project)
            .join(member.blokey, blokey)
            .where(
                member.blokey.id.eq(blokeyUuid),
                buildPredicate(dto)
            )
            .distinct();
    }

    private Long countQuery(ProjectReqReadDto dto, UUID blokeyUuid) {
        return queryFactory.select(project.count())
            .from(member)
            .join(member.project, project)
            .join(member.blokey, blokey)
            .where(
                    member.blokey.id.eq(blokeyUuid),
                    buildPredicate(dto)
            )
            .fetchOne();
    }

    private BooleanBuilder buildPredicate(ProjectReqReadDto dto) {
        BooleanBuilder builder = new BooleanBuilder();

        if (dto.getTitle() != null) builder.and(project.title.contains(dto.getTitle()));
        if (dto.getDescription() != null) builder.and(project.description.contains(dto.getDescription()));
        if (dto.getIsPrivate() != null) builder.and(project.isPrivate.eq(dto.getIsPrivate()));
        if (dto.getStatus() != null) builder.and(project.status.eq(dto.getStatus()));

        if (dto.getActualStartFrom() != null && dto.getActualEndTo() != null) {
            builder.and(project.actualStartDate.loe(dto.getActualEndTo()));
            builder.and(project.actualEndDate.goe(dto.getActualStartFrom()));
        } else if (dto.getActualStartFrom() != null) {
            builder.and(project.actualEndDate.goe(dto.getActualStartFrom()).or(project.actualEndDate.isNull()));
        } else if (dto.getActualEndTo() != null) {
            builder.and(project.actualStartDate.loe(dto.getActualEndTo()).or(project.actualStartDate.isNull()));
        }

        return builder;
    }
}
