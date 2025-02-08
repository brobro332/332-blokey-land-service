package kr.co.co_working.member.service;

import ch.qos.logback.core.util.StringUtil;
import kr.co.co_working.member.Member;
import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.dto.MemberResponseDto;
import kr.co.co_working.member.repository.MemberDslRepository;
import kr.co.co_working.member.repository.MemberRepository;
import kr.co.co_working.memberWorkspace.repository.MemberWorkspaceRepository;
import kr.co.co_working.workspace.dto.WorkspaceRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository repository;
    private final MemberDslRepository dslRepository;
    private final MemberWorkspaceRepository memberWorkspaceRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * createMember : Member 등록
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    public String createMember(MemberRequestDto.CREATE dto) throws NoSuchElementException, Exception {
        // 1. Member 빌드
        Member member = Member.builder()
            .email(dto.getEmail())
            .password(passwordEncoder.encode(dto.getPassword()))
            .name(dto.getName())
            .description(StringUtil.nullStringToEmpty(dto.getDescription()))
            .build();

        // 2. Member 등록
        repository.save(member);

        // 3. Email 반환
        return member.getEmail();
    }

    /**
     * readMember : Member 조회
     * @return
     * @throws Exception
     */
    public MemberResponseDto readMember() throws Exception {
        // 1. Member 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Member member = repository.findById(email).orElseThrow(() -> new Exception("조회하려는 멤버가 존재하지 않습니다. EMAIL :" + email));

        // 2. Member 반환
        return new MemberResponseDto(
            member.getEmail(),
            member.getName(),
            member.getDescription(),
            member.getCreatedAt(),
            member.getModifiedAt()
        );
    }

    /**
     * readMembers : Members 조회
     * @param dto
     * @return
     * @throws Exception
     */
    public List<MemberResponseDto> readMembers(MemberRequestDto.READ dto) throws Exception {
        return dslRepository.readMembers(dto);
    }

    /**
     * readMembersInWorkspace : 특정 Workspace 소속 Members 조회
     * @param dto
     * @return
     * @throws Exception
     */
    public List<MemberResponseDto> readMembersInWorkspace(WorkspaceRequestDto.READ dto) throws Exception {
        return dslRepository.readMembersInWorkspace(dto);
    }

    /**
     * readMembersNotInWorkspace : 특정 Workspace 미가입 Members 조회
     * @param dto
     * @return
     * @throws Exception
     */
    public List<MemberResponseDto> readMembersNotInWorkspace(WorkspaceRequestDto.READ dto) throws Exception {
        return dslRepository.readMembersNotInWorkspace(dto);
    }

    /**
     * updateMember : Member 수정
     * @param dto
     * @throws NoSuchElementException
     * @throws Exception
     */
    @Transactional
    public void updateMember(MemberRequestDto.UPDATE dto) throws NoSuchElementException, Exception {
        // 1. Member 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<Member> selectedMember = repository.findById(email);

        // 2. 부재 시 예외 처리
        if (selectedMember.isEmpty()) {
            throw new NoSuchElementException("수정하려는 멤버가 존재하지 않습니다. EMAIL : " + email);
        }

        // 3. Member 추출
        Member member = selectedMember.get();

        // 4. Member 수정
        member.updateMember(dto.getName(), dto.getDescription());
    }

    /**
     * updatePassword : Member 비밀번호 수정
     * @param dto
     * @throws NoSuchElementException
     * @throws Exception
     */
    @Transactional
    public void updatePassword(MemberRequestDto.PASSWORD dto) throws NoSuchElementException, Exception {
        // 1. Member 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<Member> selectedMember = repository.findById(email);

        // 2. 부재 시 예외 처리
        if (selectedMember.isEmpty()) {
            throw new NoSuchElementException("수정하려는 멤버가 존재하지 않습니다. EMAIL : " + email);
        }

        // 3. Member 추출
        Member member = selectedMember.get();

        // 4. 비밀번호 수정
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        member.updatePassword(encodedPassword);
    }

    /**
     * deleteMember : Member 삭제
     * @throws NoSuchElementException
     * @throws Exception
     */
    @Transactional
    public void deleteMember() throws NoSuchElementException, Exception {
        // 1. Member 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<Member> selectedMember = repository.findById(email);

        // 2. 부재 시 예외 처리
        if (selectedMember.isEmpty()) {
            throw new NoSuchElementException("수정하려는 멤버가 존재하지 않습니다. EMAIL : " + email);
        }

        // 3. Member 객체 추출
        Member member = selectedMember.get();

        // 4. Member 삭제
        member.updateDelFlag("1");
    }
}