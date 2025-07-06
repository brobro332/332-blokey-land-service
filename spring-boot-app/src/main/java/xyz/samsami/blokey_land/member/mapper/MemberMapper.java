package xyz.samsami.blokey_land.member.mapper;

import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.member.domain.Member;
import xyz.samsami.blokey_land.member.type.RoleType;
import xyz.samsami.blokey_land.project.domain.Project;

public class MemberMapper {
    public static Member toEntity(Project project, Blokey blokey, RoleType role) {
        return Member.builder()
            .role(role)
            .project(project)
            .blokey(blokey)
            .build();
    }
}