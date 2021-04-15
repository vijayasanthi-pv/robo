package cc.robart.iot.demoproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cc.robart.iot.demoproject.persistent.Firmware;

@Repository
public interface FirmwareRepository extends JpaRepository<Firmware, String>{
	Optional<Firmware> findByName(String name);
}
