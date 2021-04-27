package cc.robo.iot.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.robo.iot.dto.Firmware;
import cc.robo.iot.dto.Robot;
import cc.robo.iot.exceptions.EntityAlreadyExistsException;
import cc.robo.iot.exceptions.NotFoundException;
import cc.robo.iot.persistent.FirmwareEntity;
import cc.robo.iot.persistent.RobotEntity;
import cc.robo.iot.repository.RobotRepository;
import cc.robo.iot.utils.DomainModelToViewConverter;

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
	
	@Autowired
	public RobotService(RobotRepository repository, IFirmwareService firmwareService,
			DomainModelToViewConverter domainModelToViewConverter) {
		super();
		this.repository = repository;
		this.firmwareService = firmwareService;
		this.domainModelToViewConverter = domainModelToViewConverter;
	}

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
	 */
	@Override
	public void assignFirmware(String firmwareName, List<String> robotNames) {
		Optional<FirmwareEntity> firmwareOptional = firmwareService.findByName(firmwareName);
		if (firmwareOptional.isPresent()) {
			FirmwareEntity firmware = firmwareOptional.get();
			
			Stream<String> robotsStream = robotNames.stream();
			if (robotsStream.anyMatch(name->repository.findByName(name).equals(Optional.empty())))
				throw new NotFoundException("Some robots does not exist");
			else{List<RobotEntity> robotEntities = repository.findByNameIn(robotNames);
			robotEntities.stream().forEach(robotEntity->robotEntity.setFirmware(firmware));
			repository.saveAll(robotEntities);}
		}else {
			throw new NotFoundException("Firmware with the name "+firmwareName+" does not exist");
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
