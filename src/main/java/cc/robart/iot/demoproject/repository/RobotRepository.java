package cc.robart.iot.demoproject.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cc.robart.iot.demoproject.persistent.Robot;

@Repository
public interface RobotRepository extends JpaRepository<Robot, String>{
	
	Optional<Robot> findByName(String name);
	
	@Modifying
	@Query("update robot set fk_firmware_id = ?1 where id = ?2")
	void assignFirmware(@Param(value = "fk_firmware_id") UUID fk_firmware_id, @Param(value = "id") UUID id);
}
