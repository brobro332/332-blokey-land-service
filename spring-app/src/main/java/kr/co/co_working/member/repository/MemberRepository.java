package kr.co.co_working.member.repository;

import kr.co.co_working.member.Member;
import kr.co.co_working.member.dto.MemberResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
