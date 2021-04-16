package cc.robart.iot.demoproject.service;

import java.util.List;

import cc.robart.iot.demoproject.dto.FirmwareDTO;
import cc.robart.iot.demoproject.dto.RobotDTO;

public interface IRobotService {

	List<RobotDTO> list();
	
	FirmwareDTO latestFirmware(String name);

	void assignFirmware(String firmwareName, List<String> robotNames);
}
