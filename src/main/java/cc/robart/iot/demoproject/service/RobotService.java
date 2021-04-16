package cc.robart.iot.demoproject.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.robart.iot.demoproject.exceptions.NotFoundException;
import cc.robart.iot.demoproject.persistent.Firmware;
import cc.robart.iot.demoproject.persistent.Robot;
import cc.robart.iot.demoproject.repository.RobotRepository;

@Service
public class RobotService implements IRobotService{

	Logger logger = LoggerFactory.getLogger(RobotService.class);
	
	@Autowired
	private RobotRepository repository;
	
	@Autowired
	private IFirmwareService firmwareService;
	
	@Override
	public List<Robot> list() {
		return repository.findAll();
	}

	@Override
	public Firmware latestFirmware(String name) {
		Optional<Robot> optional = repository.findByName(name);
		if (optional.isPresent()) {
			return optional.get().getHardwareVersion();
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
