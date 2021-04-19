package cc.robart.iot.demoproject.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import cc.robart.iot.demoproject.persistent.RobotEntity;

/**
 * Repository for Robot
 *
 */
@JaversSpringDataAuditable
public interface RobotRepository extends JpaRepository<RobotEntity, UUID>{
	Optional<RobotEntity> findByName(String name);
	List<RobotEntity> findByNameIn(List<String> robotNames);
}
