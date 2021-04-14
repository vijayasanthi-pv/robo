package cc.robart.iot.demoproject.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cc.robart.iot.demoproject.exceptions.NotFoundException;
import cc.robart.iot.demoproject.persistent.Firmware;
import cc.robart.iot.demoproject.repository.FirmwareRepository;

@Service
public class FirmwareService implements IFirmwareService{
	
	Logger logger = LoggerFactory.getLogger(FirmwareService.class);
	
	@Autowired
	private FirmwareRepository repository;
	
	@Autowired
	private IRobotService robotService;

	@Override
	public List<Firmware> list() {
		return repository.findAll();
	}

	@Override
	public Firmware add(@Valid Firmware firmware) {
		return repository.save(firmware);
	}

	@Override
	public void delete(String name) {
		Optional<Firmware> optional = repository.findByName(name);
		if(optional.isPresent()) {
			repository.delete(optional.get());
		}else {
			throw new NotFoundException("Firmware with the name "+name+" doesn't exist");
		}
	}

	@Override
	public Firmware update(String name, Firmware firmware) {
		Optional<Firmware> optional = repository.findByName(name);
		if (optional.isPresent()) {
			try {
				BeanUtils.copyProperties(optional.get(), firmware);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			return repository.save(firmware);
		}else {
			throw new NotFoundException("Firmware with the name "+name+" doesnot exist");
		}
	}

	@Override
	public void assignFirmware(String name, List<String> robotNames) {
			robotService.assignFirmware(name, robotNames);
	}

}
