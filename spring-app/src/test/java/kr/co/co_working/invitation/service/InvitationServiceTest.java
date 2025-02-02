package kr.co.co_working.invitation.service;

import kr.co.co_working.invitation.Invitation;
import kr.co.co_working.invitation.RequesterType;
import kr.co.co_working.invitation.dto.InvitationRequestDto;
import kr.co.co_working.invitation.dto.InvitationResponseDto;
import kr.co.co_working.invitation.repository.InvitationRepository;
import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.service.MemberService;
import kr.co.co_working.workspace.dto.WorkspaceRequestDto;
import kr.co.co_working.workspace.service.WorkspaceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional

class InvitationServiceTest {
    @Autowired
    InvitationService service;

    @Autowired
    InvitationRepository repository;

    @Autowired
    WorkspaceService workspaceService;

    @Autowired
    MemberService memberService;

    @BeforeEach
    void setUp() {
        String email = "test@korea.kr";
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            email, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void createInvitation() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberCreateDto = getCreateMemberDto();
        String email = memberService.createMember(memberCreateDto);

        WorkspaceRequestDto.CREATE workspaceCreateDto = getCreateWorkspaceDto();
        Long workspaceId = workspaceService.createWorkspace(workspaceCreateDto);

        InvitationRequestDto.CREATE invitationCreateDto = new InvitationRequestDto.CREATE();
        invitationCreateDto.setWorkspaceId(workspaceId);
        invitationCreateDto.setMemberId(email);
        invitationCreateDto.setRequesterType("WORKSPACE");

        /* when */
        Long id = service.createInvitation(invitationCreateDto);

        /* then */
        Invitation invitation = repository.findById(id).get();
        Assertions.assertEquals(workspaceId, invitation.getWorkspace().getId());
        Assertions.assertEquals(email, invitation.getMember().getEmail());
        Assertions.assertEquals(RequesterType.WORKSPACE, invitation.getRequesterType());
    }

    @Test
    public void readInvitation() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberCreateDto = getCreateMemberDto();
        String email = memberService.createMember(memberCreateDto);

        WorkspaceRequestDto.CREATE workspaceCreateDto = getCreateWorkspaceDto();
        Long workspaceId = workspaceService.createWorkspace(workspaceCreateDto);

        InvitationRequestDto.CREATE invitationCreateDto = getCreateInvitationDto(email, workspaceId);

        /* when */
        service.createInvitation(invitationCreateDto);

        InvitationRequestDto.READ invitationReadDto = new InvitationRequestDto.READ();
        invitationReadDto.setEmail("test");
        invitationReadDto.setName("김");
        invitationReadDto.setDivision("WORKSPACE");
        List<InvitationResponseDto> invitations = service.readInvitation(invitationReadDto);

        /* then */
        Assertions.assertEquals(1, invitations.size());
    }

    @Test
    public void deleteInvitation() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberCreateDto = getCreateMemberDto();
        String email = memberService.createMember(memberCreateDto);

        WorkspaceRequestDto.CREATE workspaceCreateDto = getCreateWorkspaceDto();
        Long workspaceId = workspaceService.createWorkspace(workspaceCreateDto);

        InvitationRequestDto.CREATE invitationCreateDto1 = new InvitationRequestDto.CREATE();
        invitationCreateDto1.setWorkspaceId(workspaceId);
        invitationCreateDto1.setMemberId(email);
        invitationCreateDto1.setRequesterType("WORKSPACE");

        InvitationRequestDto.CREATE invitationCreateDto2 = new InvitationRequestDto.CREATE();
        invitationCreateDto2.setWorkspaceId(workspaceId);
        invitationCreateDto2.setMemberId(email);
        invitationCreateDto2.setRequesterType("MEMBER");

        /* when */
        Long id = service.createInvitation(invitationCreateDto1);
        service.createInvitation(invitationCreateDto2);

        InvitationRequestDto.DELETE invitationDeleteDto = new InvitationRequestDto.DELETE();
        invitationDeleteDto.setId(id);
        service.deleteInvitation(invitationDeleteDto);

        /* then */
        List<Invitation> invitationList = repository.findAll();
        Assertions.assertEquals(1, invitationList.size());
    }

    /**
     * getCreateDto : Member CREATE DTO 반환
     * @return
     */
    private static MemberRequestDto.CREATE getCreateMemberDto() {
        MemberRequestDto.CREATE memberDto = new MemberRequestDto.CREATE();
        memberDto.setEmail("test@korea.kr");
        memberDto.setPassword("1234");
        memberDto.setName("김아무개");
        memberDto.setDescription("test");
        return memberDto;
    }

    /**
     * getCreateWorkspaceDto : Workspace CREATE DTO 반환
     * @return
     */
    private static WorkspaceRequestDto.CREATE getCreateWorkspaceDto() {
        WorkspaceRequestDto.CREATE dto = new WorkspaceRequestDto.CREATE();
        dto.setName("팀명 1");
        dto.setDescription("팀 소개입니다.");
        return dto;
    }

    /**
     * getCreateInvitationDto : Invitation CREATE DTO 반환
     * @param email
     * @param workspaceId
     * @return
     */
    private static InvitationRequestDto.CREATE getCreateInvitationDto(String email, Long workspaceId) {
        InvitationRequestDto.CREATE dto = new InvitationRequestDto.CREATE();
        dto.setMemberId(email);
        dto.setWorkspaceId(workspaceId);
        dto.setRequesterType(RequesterType.WORKSPACE.name());
        return dto;
    }
}