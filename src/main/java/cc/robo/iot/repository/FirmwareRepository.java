package cc.robo.iot.repository;

import java.util.Optional;
import java.util.UUID;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import cc.robo.iot.persistent.FirmwareEntity;

/**
 * Repository for Firmware
 *
 */
@JaversSpringDataAuditable
public interface FirmwareRepository extends JpaRepository<FirmwareEntity, UUID>{
	Optional<FirmwareEntity> findByName(String name);
}
