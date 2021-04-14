package cc.robart.iot.demoproject.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.robart.iot.demoproject.exceptions.NotFoundException;
import cc.robart.iot.demoproject.persistent.Robot;
import cc.robart.iot.demoproject.repository.RobotRepository;

@Service
public class RobotService implements IRobotService{

	Logger logger = LoggerFactory.getLogger(RobotService.class);
	
	@Autowired
	private RobotRepository repository;
	
	@Override
	public List<Robot> list() {
		return repository.findAll();
	}

	@Override
	public Robot update(String name, Robot robot) {
		Optional<Robot> optional = repository.findByName(name);
		if (optional.isPresent()) {
			Robot existingRobot = optional.get();
			//If the robot name is not changed, update the existing object
			if(existingRobot.getName().equals(robot.getName())) {
				try {
					BeanUtils.copyProperties(existingRobot, robot);
					return repository.save(existingRobot);
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			robot.setName(existingRobot.getName());
			robot.setHardwareVersion(existingRobot.getHardwareVersion());
			repository.delete(existingRobot);
			return repository.save(robot);
		}else {
			throw new NotFoundException("Robot with the name "+name+" doesnot exist");
		}
	}

	@Override
	public void assignFirmware(String firmwareName, List<String> robotNames) {
		
		List<Robot> robots = new ArrayList<>();
		for (String name:robotNames) {
			Optional<Robot> optional = repository.findByName(name);
			if(optional.isPresent()) {
				Robot robot = optional.get();
			}
		}
	}

}
