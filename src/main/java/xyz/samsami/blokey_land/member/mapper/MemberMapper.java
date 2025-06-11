package xyz.samsami.blokey_land.member.mapper;

import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.member.domain.Member;
import xyz.samsami.blokey_land.member.dto.MemberReqCreateDto;
import xyz.samsami.blokey_land.project.domain.Project;

public class MemberMapper {
    public static Member toEntity(Project project, Blokey blokey, MemberReqCreateDto dto) {
        return Member.builder()
            .role(dto.getRole())
            .project(project)
            .blokey(blokey)
            .build();
    }
}