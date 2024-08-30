package kr.co.co_working.member.service;

import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.dto.MemberResponseDto;
import kr.co.co_working.member.repository.MemberRepository;
import kr.co.co_working.member.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    MemberService service;

    @Autowired
    MemberRepository repository;

    @Test
    public void createMember() throws Exception {
        /* given */
        MemberRequestDto.CREATE dto = getCreateDto();

        /* when */
        service.createMember(dto);

        /* then */
        Optional<Member> selectedMember = repository.findById(dto.getEmail());
        Member member = selectedMember.get();

        Assertions.assertEquals("test@korea.kr", member.getEmail());
        Assertions.assertEquals("1234", member.getPassword());
        Assertions.assertEquals("김아무개", member.getName());
        Assertions.assertEquals("test", member.getDescription());
    }

    @Test
    public void readMemberList() throws Exception {
        /* given */
        MemberRequestDto.CREATE createDto = getCreateDto();
        service.createMember(createDto);
        MemberRequestDto.READ readDto = new MemberRequestDto.READ("test@korea.kr", "무개");

        /* when */
        List<MemberResponseDto> members = service.readMemberList(readDto);

        /* then */
        MemberResponseDto member = members.get(0);
        Assertions.assertEquals(1, members.size());
        Assertions.assertEquals("test@korea.kr", member.getEmail());
        Assertions.assertEquals("김아무개", member.getName());
        Assertions.assertEquals("test", member.getDescription());
    }

    @Test
    public void updateMember() throws Exception {
        /* given */
        MemberRequestDto.CREATE createDto = getCreateDto();
        service.createMember(createDto);

        MemberRequestDto.UPDATE updateDto = MemberRequestDto.UPDATE.builder()
                .email("test@korea.kr")
                .name("박아무개")
                .description("수정입니다.")
                .build();

        /* when */
        service.updateMember(updateDto);

        /* then */
        Optional<Member> selectedMember = repository.findById(createDto.getEmail());
        Member member = selectedMember.get();

        Assertions.assertEquals("test@korea.kr", member.getEmail());
        Assertions.assertEquals("박아무개", member.getName());
        Assertions.assertEquals("수정입니다.", member.getDescription());
    }
    
    @Test
    public void deleteMember() throws Exception {
        /* given */
        MemberRequestDto.CREATE createDto = getCreateDto();
        service.createMember(createDto);

        MemberRequestDto.DELETE deleteDto = new MemberRequestDto.DELETE();
        deleteDto.setEmail(createDto.getEmail());

        /* when */
        service.deleteMember(deleteDto);
        
        /* then */
        MemberRequestDto.READ readDto = new MemberRequestDto.READ();
        List<MemberResponseDto> members = service.readMemberList(readDto);

        Assertions.assertEquals(0, members.size());
    }

    /**
     * getCreateDto : Member CREATE DTO 반환
     * @return
     */
    private static MemberRequestDto.CREATE getCreateDto() {
        MemberRequestDto.CREATE dto = new MemberRequestDto.CREATE();
        dto.setEmail("test@korea.kr");
        dto.setPassword("1234");
        dto.setName("김아무개");
        dto.setDescription("test");

        return dto;
    }
}