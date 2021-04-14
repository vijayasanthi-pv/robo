package cc.robart.iot.demoproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cc.robart.iot.demoproject.persistent.Robot;

@Repository
public interface RobotRepository extends JpaRepository<Robot, String>{
	Optional<Robot> findByName(String name);
}
