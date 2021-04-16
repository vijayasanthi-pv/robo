package cc.robart.iot.demoproject.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.javers.spring.annotation.JaversAuditable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.robart.iot.demoproject.dto.FirmwareDTO;
import cc.robart.iot.demoproject.dto.RobotDTO;
import cc.robart.iot.demoproject.exceptions.NotFoundException;
import cc.robart.iot.demoproject.persistent.Firmware;
import cc.robart.iot.demoproject.persistent.Robot;
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
	public List<RobotDTO> list() {
		return repository.findAll().stream().map(robot->domainModelToViewConverter.robotToRobotDto(robot)).collect(Collectors.toList());
	}

	@Override
	public FirmwareDTO latestFirmware(String name) {
		Optional<Robot> optional = repository.findByName(name);
		if (optional.isPresent()) {
			return domainModelToViewConverter.firmwareToFirmwareDto(optional.get().getHardwareVersion());
		}else {
			throw new NotFoundException("Robot with name "+name+" doesn't exist");
		} 
	}

	@Transactional
	@Override
	public void assignFirmware(String firmwareName, List<String> robotNames) {
		Optional<Firmware> firmwareOptional = firmwareService.findByName(firmwareName);
		if (firmwareOptional.isPresent()) {
			Firmware firmware = firmwareOptional.get();
			robotNames.stream().map(name->repository.findByName(name))
			   .filter(optional->optional.isPresent())
			   .forEach(optional->{
				   repository.assignFirmware(firmware.getId(),optional.get().getId());
			   });
		}else {
			throw new NotFoundException("Firmware with the name "+firmwareName+" doesnot exist");
		}
	}
	

}
