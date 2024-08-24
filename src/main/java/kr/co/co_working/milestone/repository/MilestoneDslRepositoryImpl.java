package kr.co.co_working.milestone.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.co_working.milestone.dto.MilestoneRequestDto;
import kr.co.co_working.milestone.dto.MilestoneResponseDto;
import kr.co.co_working.milestone.dto.QMilestoneResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.co_working.milestone.QMilestone.milestone;


@Repository
@RequiredArgsConstructor
public class MilestoneDslRepositoryImpl implements MilestoneDslRepository {
    private final JPAQueryFactory factory;

    @Override
    public List<MilestoneResponseDto> readMilestoneList(MilestoneRequestDto.READ dto) {
        return factory
                .select(new QMilestoneResponseDto(milestone.id, milestone.name, milestone.description, milestone.dueAt, milestone.createdAt, milestone.modifiedAt))
                .from(milestone)
                .where(nameContains(dto.getName()))
                .where(descriptionContains(dto.getDescription()))
                .fetch();
    }

    private BooleanExpression nameContains(String nameCond) {
        return nameCond != null ? milestone.name.contains(nameCond) : null;
    }

    private BooleanExpression descriptionContains(String descriptionCond) {
        return descriptionCond != null ? milestone.name.contains(descriptionCond) : null;
    }
}