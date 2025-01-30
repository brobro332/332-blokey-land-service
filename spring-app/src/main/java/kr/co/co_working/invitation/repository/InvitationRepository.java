package kr.co.co_working.invitation.repository;

import kr.co.co_working.invitation.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
}
