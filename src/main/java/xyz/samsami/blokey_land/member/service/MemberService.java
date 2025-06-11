package xyz.samsami.blokey_land.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.member.domain.Member;
import xyz.samsami.blokey_land.member.dto.MemberReqCreateDto;
import xyz.samsami.blokey_land.member.dto.MemberReqDeleteDto;
import xyz.samsami.blokey_land.member.dto.MemberReqUpdateDto;
import xyz.samsami.blokey_land.member.dto.MemberRespDto;
import xyz.samsami.blokey_land.member.mapper.MemberMapper;
import xyz.samsami.blokey_land.member.repository.MemberRepository;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.service.ProjectService;
import xyz.samsami.blokey_land.user.domain.User;
import xyz.samsami.blokey_land.user.service.UserService;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final ProjectService projectService;
    private final UserService userService;
    private final MemberRepository repository;

    @Transactional
    public void createMember(Long projectId, MemberReqCreateDto dto) {
        Project project = projectService.findProjectByProjectId(projectId);
        User user = userService.findUserByUserId(dto.getUserId());

        if (project != null && user != null) {
            Member member = MemberMapper.toEntity(project, user, dto);
            repository.save(member);
        }
    }

    public Page<MemberRespDto> readMemberByProjectId(Long projectId, Pageable pageable) {
        return repository.findDtoByProjectId(projectId, pageable);
    }

    public Page<MemberRespDto> readMemberByUserId(UUID userId, Pageable pageable) {
        return repository.findDtoByUserId(userId, pageable);
    }

    @Transactional
    public void updateMemberByMemberId(Long memberId, MemberReqUpdateDto dto) {
        Member member = findMemberByMemberId(memberId);
        if (member != null) member.updateRole(dto.getRole());
    }

    @Transactional
    public void deleteMember(Long projectId, MemberReqDeleteDto dto) {
        Project project = projectService.findProjectByProjectId(projectId);
        User user = userService.findUserByUserId(dto.getUserId());

        if (project != null && user != null) {
            Member member = findMemberByProjectAndUser(project, user);
            repository.delete(member);
        }
    }

    public Member findMemberByMemberId(Long memberId) {
        return repository.findById(memberId).orElseThrow(() ->
            new CommonException(ExceptionType.NOT_FOUND, null)
        );
    }

    public Member findMemberByProjectAndUser(Project project, User user) {
        return repository.findByProjectAndUser(project, user).orElseThrow(() ->
            new CommonException(ExceptionType.NOT_FOUND, null)
        );
    }
}