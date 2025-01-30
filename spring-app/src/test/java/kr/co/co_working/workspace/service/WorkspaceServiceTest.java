package kr.co.co_working.workspace.service;

import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.service.MemberService;
import kr.co.co_working.memberWorkspace.MemberWorkspace;
import kr.co.co_working.memberWorkspace.repository.MemberWorkspaceRepository;
import kr.co.co_working.workspace.Workspace;
import kr.co.co_working.workspace.dto.WorkspaceRequestDto;
import kr.co.co_working.workspace.dto.WorkspaceResponseDto;
import kr.co.co_working.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class WorkspaceServiceTest {
    @Autowired
    WorkspaceService service;

    @Autowired
    MemberService memberService;

    @Autowired
    WorkspaceRepository repository;

    @Autowired
    MemberWorkspaceRepository memberWorkspaceRepository;

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
    public void createWorkspace() throws Exception {
        /* given */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        WorkspaceRequestDto.CREATE dto = getCreateWorkspaceDto(memberDto);

        /* when */
        Long id = service.createWorkspace(dto);

        /* then */
        Workspace workspace = repository.findById(id).get();
        Assertions.assertEquals("팀명 1", workspace.getName());
        Assertions.assertEquals("팀 소개입니다.", workspace.getDescription());

        List<MemberWorkspace> memberWorkspace = memberWorkspaceRepository.findByWorkspaceId(id);
        Assertions.assertEquals(id, memberWorkspace.get(0).getWorkspace().getId());
        Assertions.assertEquals(email, memberWorkspace.get(0).getMember().getEmail());
    }



    @Test
    public void readWorkspace() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        WorkspaceRequestDto.CREATE createDto = getCreateWorkspaceDto(memberDto);
        service.createWorkspace(createDto);

        WorkspaceRequestDto.READ readDto = new WorkspaceRequestDto.READ();

        /* when */
        List<WorkspaceResponseDto> Workspaces = service.readWorkspace(readDto);
        
        /* then */
        Assertions.assertEquals(1, Workspaces.size());
        Assertions.assertEquals("팀명 1", Workspaces.get(0).getName());
        Assertions.assertEquals("팀 소개입니다.", Workspaces.get(0).getDescription());
    }

    @Test
    public void updateWorkspace() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        WorkspaceRequestDto.CREATE createDto = getCreateWorkspaceDto(memberDto);

        Long id = service.createWorkspace(createDto);

        WorkspaceRequestDto.UPDATE updateDto = new WorkspaceRequestDto.UPDATE();
        updateDto.setName("팀명 2");
        updateDto.setDescription("팀 소개 수정입니다.");

        /* when */
        service.updateWorkspace(id, updateDto);

        /* then */
        Workspace workspace = repository.findById(id).get();
        Assertions.assertEquals("팀명 2", workspace.getName());
        Assertions.assertEquals("팀 소개 수정입니다.", workspace.getDescription());
    }

    @Test
    public void deleteWorkspace() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        List<Long> idList = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            WorkspaceRequestDto.CREATE createDto = new WorkspaceRequestDto.CREATE();
            createDto.setName("팀명 " + i);
            createDto.setDescription("팀 소개입니다. " + i);
            idList.add(service.createWorkspace(createDto));
            createDto = null;
        }

        /* when */
        Workspace workspace = repository.findById(idList.get(0)).get();
        service.deleteWorkspace(workspace.getId());

        /* then */
        List<Workspace> workspaces = repository.findAll();

        Assertions.assertEquals(1, workspaces.size());
        Assertions.assertEquals("팀명 2", workspaces.get(0).getName());
        Assertions.assertEquals("팀 소개입니다. 2", workspaces.get(0).getDescription());
    }

    @Test
    public void addMemberToWorkspace() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        String email = memberService.createMember(memberDto);

        WorkspaceRequestDto.CREATE WorkspaceDto = getCreateWorkspaceDto(memberDto);
        Long workspaceId = service.createWorkspace(WorkspaceDto);

        /* when */
        service.addMemberToWorkspace(workspaceId, email);

        /* then */
        List<MemberWorkspace> memberWorkspaceList = memberWorkspaceRepository.findByWorkspaceId(workspaceId);
        Assertions.assertEquals(2, memberWorkspaceList.size());
    }

    @Test
    public void removeMemberToWorkspace() throws Exception {
        /* given */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        WorkspaceRequestDto.CREATE workspaceDto = getCreateWorkspaceDto(memberDto);
        Long workspaceId = service.createWorkspace(workspaceDto);

        /* when */
        service.removeMemberFromWorkspace(email, workspaceId);

        /* then */
        List<MemberWorkspace> memberWorkspace = memberWorkspaceRepository.findByWorkspaceId(workspaceId);
        Assertions.assertEquals(0, memberWorkspace.size());
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
     * @param memberDto
     * @return
     */
    private static WorkspaceRequestDto.CREATE getCreateWorkspaceDto(MemberRequestDto.CREATE memberDto) {
        WorkspaceRequestDto.CREATE dto = new WorkspaceRequestDto.CREATE();
        dto.setName("팀명 1");
        dto.setDescription("팀 소개입니다.");
        return dto;
    }
}