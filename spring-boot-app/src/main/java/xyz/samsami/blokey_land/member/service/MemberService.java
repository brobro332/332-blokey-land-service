package xyz.samsami.blokey_land.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.member.domain.Member;
import xyz.samsami.blokey_land.member.dto.MemberReqUpdateDto;
import xyz.samsami.blokey_land.member.dto.MemberRespDto;
import xyz.samsami.blokey_land.member.mapper.MemberMapper;
import xyz.samsami.blokey_land.member.repository.MemberRepository;
import xyz.samsami.blokey_land.member.type.RoleType;
import xyz.samsami.blokey_land.project.domain.Project;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository repository;

    @Transactional
    public void createMember(Project project, Blokey blokey, RoleType role) {
        if (project != null && blokey != null) {
            Member member = MemberMapper.toEntity(project, blokey, role);
            repository.save(member);
        }
    }

    public Page<MemberRespDto> readMemberByProjectId(Long projectId, Pageable pageable) {
        return repository.findDtoByProjectId(projectId, pageable);
    }

    public Page<MemberRespDto> readMemberByBlokeyId(UUID blokeyId, Pageable pageable) {
        return repository.findDtoByBlokeyId(blokeyId, pageable);
    }

    @Transactional
    public void updateMemberByMemberId(Long memberId, MemberReqUpdateDto dto) {
        Member member = findMemberByMemberId(memberId);
        if (member != null) member.updateRole(dto.getRole());
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = findMemberByMemberId(memberId);
        if (memberId != null) repository.delete(member);
    }

    public Member findMemberByMemberId(Long memberId) {
        return repository.findById(memberId).orElseThrow(() ->
            new CommonException(ExceptionType.NOT_FOUND, null)
        );
    }
}