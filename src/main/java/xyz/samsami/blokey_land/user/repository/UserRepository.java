package xyz.samsami.blokey_land.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.samsami.blokey_land.user.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findOptionalById(UUID id);
    Page<User> findPageAll(Pageable pageable);
}