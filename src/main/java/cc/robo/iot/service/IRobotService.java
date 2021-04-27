package cc.robo.iot.service;

import java.util.List;

import cc.robo.iot.dto.Firmware;
import cc.robo.iot.dto.Robot;

/**
 * Interface for Robot
 *
 */
public interface IRobotService {

	List<Robot> list();
	
	Firmware latestFirmware(String name);

	void assignFirmware(String firmwareName, List<String> robotNames);

	Robot create(Robot robot);
}
