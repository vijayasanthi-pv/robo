package cc.robart.iot.demoproject.repository;

import java.util.Optional;
import java.util.UUID;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import cc.robart.iot.demoproject.persistent.FirmwareEntity;

@JaversSpringDataAuditable
public interface FirmwareRepository extends JpaRepository<FirmwareEntity, UUID>{
	Optional<FirmwareEntity> findByName(String name);
}
