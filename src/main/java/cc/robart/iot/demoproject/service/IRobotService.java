package cc.robart.iot.demoproject.service;

import java.util.List;
import java.util.UUID;

import cc.robart.iot.demoproject.persistent.Firmware;
import cc.robart.iot.demoproject.persistent.Robot;

public interface IRobotService {

	List<Robot> list();
	
	void assignFirmware(UUID firmwareId, List<String> robotNames);

	Firmware latestFirmware(String name);
}
