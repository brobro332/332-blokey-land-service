package kr.co.co_working.member.repository;

import kr.co.co_working.member.repository.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
