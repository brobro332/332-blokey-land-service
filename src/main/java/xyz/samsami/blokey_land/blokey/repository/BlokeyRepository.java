package xyz.samsami.blokey_land.blokey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.samsami.blokey_land.blokey.domain.Blokey;

import java.util.UUID;

public interface BlokeyRepository extends JpaRepository<Blokey, UUID> {
}