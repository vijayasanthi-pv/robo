package cc.robart.iot.demoproject.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cc.robart.iot.demoproject.persistent.UserEntity;

/**
 * Repository for User
 *
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID>{
	Optional<UserEntity> findByName(String name);
	Optional<UserEntity> findByEmail(String email);
}
