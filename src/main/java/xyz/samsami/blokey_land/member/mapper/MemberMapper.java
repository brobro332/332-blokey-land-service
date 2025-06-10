package xyz.samsami.blokey_land.member.mapper;

import xyz.samsami.blokey_land.member.domain.Member;
import xyz.samsami.blokey_land.member.dto.MemberReqCreateDto;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.user.domain.User;

public class MemberMapper {
    public static Member toEntity(Project project, User user, MemberReqCreateDto dto) {
        return Member.builder()
            .role(dto.getRole())
            .project(project)
            .user(user)
            .build();
    }
}