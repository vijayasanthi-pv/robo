package cc.robart.iot.demoproject.service;

import java.util.List;

import cc.robart.iot.demoproject.persistent.Firmware;
import cc.robart.iot.demoproject.persistent.Robot;

public interface IRobotService {

	List<Robot> list();
	
	Firmware latestFirmware(String name);

	void assignFirmware(String firmwareName, List<String> robotNames);
}
