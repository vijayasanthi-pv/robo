package cc.robart.iot.demoproject.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.robart.iot.demoproject.dto.Firmware;
import cc.robart.iot.demoproject.dto.Robot;
import cc.robart.iot.demoproject.exceptions.EntityAlreadyExistsException;
import cc.robart.iot.demoproject.exceptions.NotFoundException;
import cc.robart.iot.demoproject.persistent.FirmwareEntity;
import cc.robart.iot.demoproject.persistent.RobotEntity;
import cc.robart.iot.demoproject.repository.RobotRepository;
import cc.robart.iot.demoproject.utils.DomainModelToViewConverter;

/**
 * Service Implementation for Robot
 *
 */
@Service
public class RobotService implements IRobotService{

	Logger logger = LoggerFactory.getLogger(RobotService.class);

	@Autowired
	private RobotRepository repository;

	@Autowired
	private IFirmwareService firmwareService;

	@Autowired
	private DomainModelToViewConverter domainModelToViewConverter;

	/**
	 * Lists the robots
	 * @return {@List<Robot> }
	 */
	@Override
	public List<Robot> list() {
		return repository.findAll().stream().map(robotEntity->domainModelToViewConverter.convert(robotEntity,Robot.class)).collect(Collectors.toList());
	}

	/**
	 * Returns the latest firmware associated with the robot
	 * @input name - name of the firmware
	 * @return {@Firmware }
	 */
	@Override
	public Firmware latestFirmware(String name) {
		Optional<RobotEntity> optional = repository.findByName(name);
		if (optional.isPresent()) {
			return domainModelToViewConverter.convert(optional.get().getFirmware(), Firmware.class);
		}else {
			throw new NotFoundException("Robot with name "+name+" doesn't exist");
		} 
	}

	/**
	 * Assigns Firmware to the given list of robots
	 * @input firmwareName - name of the firmware
	 * @return {@List<Robot> }
	 */
	@Override
	public void assignFirmware(String firmwareName, List<String> robotNames) {
		Optional<FirmwareEntity> firmwareOptional = firmwareService.findByName(firmwareName);
		if (firmwareOptional.isPresent()) {
			FirmwareEntity firmware = firmwareOptional.get();
			Map<String, Object> robots = robotNames.stream()
						.collect(Collectors.toMap(Function.identity(), name->repository.findByName(name)));
			robots.entrySet().stream().filter(entry->!(((Optional<RobotEntity>)entry.getValue()).isPresent()))
									  .findAny().ifPresent((entry)->{throw new NotFoundException("Robot "+entry.getKey()+" doesnot exist");});
			
			robots.entrySet().stream().filter(entry->(((Optional<RobotEntity>)entry.getValue()).isPresent()))
										.map(entry->(Optional<RobotEntity>)entry.getValue())
										.forEach(optional->{
											RobotEntity robot = optional.get();
											robot.setFirmware(firmware);
											repository.save(robot);
										});
		}else {
			throw new NotFoundException("Firmware with the name "+firmwareName+" doesnot exist");
		}
	}

	/**
	 * Create the robot
	 * input robot - robot that has to be created
	 * @return {Robot }
	 */
	@Override
	public Robot create(Robot robot) {
		Optional<RobotEntity> optional = repository.findByName(robot.getName());
		if(optional.isPresent()) {
			throw new EntityAlreadyExistsException("Robot already exist");
		}else {
			RobotEntity robotEntity = repository.save(domainModelToViewConverter.convert(robot, RobotEntity.class));
			return domainModelToViewConverter.convert(robotEntity,Robot.class);
		}
	}
}
