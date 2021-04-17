package cc.robart.iot.demoproject.service;

import java.util.List;
import java.util.Optional;
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

@Service
public class RobotService implements IRobotService{

	Logger logger = LoggerFactory.getLogger(RobotService.class);
	
	@Autowired
	private RobotRepository repository;
	
	@Autowired
	private IFirmwareService firmwareService;
	
	@Autowired
	private DomainModelToViewConverter domainModelToViewConverter;
	
	@Override
	public List<Robot> list() {
		return repository.findAll().stream().map(robotEntity->domainModelToViewConverter.convert(robotEntity,Robot.class)).collect(Collectors.toList());
	}

	@Override
	public Firmware latestFirmware(String name) {
		Optional<RobotEntity> optional = repository.findByName(name);
		if (optional.isPresent()) {
			return domainModelToViewConverter.convert(optional.get().getFirmware(), Firmware.class);
		}else {
			throw new NotFoundException("Robot with name "+name+" doesn't exist");
		} 
	}

	@Override
	public void assignFirmware(String firmwareName, List<String> robotNames) {
		Optional<FirmwareEntity> firmwareOptional = firmwareService.findByName(firmwareName);
		if (firmwareOptional.isPresent()) {
			FirmwareEntity firmware = firmwareOptional.get();
			robotNames.stream().map(name->repository.findByName(name))
			   .filter(optional->optional.isPresent())
			   .forEach(optional->{
				   RobotEntity robot = optional.get();
				   robot.setFirmware(firmware);
				   repository.save(robot);
			   });
		}else {
			throw new NotFoundException("Firmware with the name "+firmwareName+" doesnot exist");
		}
	}

	@Override
	public Robot create(Robot robot) {
		Optional<RobotEntity> optional = repository.findByName(robot.getName());
		if(optional.isPresent()) {
			throw new EntityAlreadyExistsException("Robot already exist");
		}else {
			return domainModelToViewConverter.convert(repository.save(domainModelToViewConverter.convert(robot, RobotEntity.class)),Robot.class);
		}
	}
}
