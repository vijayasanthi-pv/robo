package cc.robart.iot.demoproject.service;

import java.util.List;
import cc.robart.iot.demoproject.persistent.Robot;

public interface IRobotService {

	List<Robot> list();
	
	Robot update(String name, Robot robot);

	void assignFirmware(String firmwareName, List<String> robotNames);
}
