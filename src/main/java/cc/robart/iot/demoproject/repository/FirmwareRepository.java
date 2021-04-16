package cc.robart.iot.demoproject.repository;

import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import cc.robart.iot.demoproject.persistent.Firmware;

@JaversSpringDataAuditable
public interface FirmwareRepository extends JpaRepository<Firmware, String>{
	Optional<Firmware> findByName(String name);
}
