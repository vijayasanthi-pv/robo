package cc.robart.iot.demoproject.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.robart.iot.demoproject.exceptions.EntityAlreadyExistsException;
import cc.robart.iot.demoproject.exceptions.NotFoundException;
import cc.robart.iot.demoproject.persistent.Firmware;
import cc.robart.iot.demoproject.repository.FirmwareRepository;

@Service
public class FirmwareService implements IFirmwareService{
	
	Logger logger = LoggerFactory.getLogger(FirmwareService.class);
	
	@Autowired
	private FirmwareRepository repository;
	
	@Override
	public List<Firmware> list() {
		return repository.findAll();
	}

	@Override
	public Firmware add(@Valid Firmware firmware) {
		Optional<Firmware> optional = repository.findByName(firmware.getName());
		if(optional.isPresent()) {
			throw new EntityAlreadyExistsException("Firmware already exist");
		}else {
			return repository.save(firmware);
		}
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
			firmware.setId(optional.get().getId());
			return repository.save(firmware);
		}else {
			throw new NotFoundException("Firmware with the name "+name+" doesnot exist");
		}
	}

	@Override
	public Optional<Firmware> findByName(String firmwareName) {
		return repository.findByName(firmwareName);
	}
	
	

}
