package cc.robart.iot.demoproject.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.javers.spring.annotation.JaversAuditable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.robart.iot.demoproject.dto.FirmwareDTO;
import cc.robart.iot.demoproject.exceptions.EntityAlreadyExistsException;
import cc.robart.iot.demoproject.exceptions.NotFoundException;
import cc.robart.iot.demoproject.persistent.Firmware;
import cc.robart.iot.demoproject.repository.FirmwareRepository;
import cc.robart.iot.demoproject.utils.DomainModelToViewConverter;

@Service
public class FirmwareService implements IFirmwareService{
	
	Logger logger = LoggerFactory.getLogger(FirmwareService.class);
	
	@Autowired
	private FirmwareRepository repository;
	
	@Autowired
	private DomainModelToViewConverter domainModelToViewConverter;
	
	@Override
	public List<FirmwareDTO> list() {
		return repository.findAll().stream().map(firmware->domainModelToViewConverter.firmwareToFirmwareDto(firmware)).collect(Collectors.toList());
	}

	@Override
	public FirmwareDTO add(@Valid Firmware firmware) {
		Optional<Firmware> optional = repository.findByName(firmware.getName());
		if(optional.isPresent()) {
			throw new EntityAlreadyExistsException("Firmware already exist");
		}else {
			return domainModelToViewConverter.firmwareToFirmwareDto(repository.save(firmware));
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
	public FirmwareDTO update(String name, Firmware firmware) {
		Optional<Firmware> optional = repository.findByName(name);
		if (optional.isPresent()) {
			firmware.setId(optional.get().getId());
			return domainModelToViewConverter.firmwareToFirmwareDto(repository.save(firmware));
		}else {
			throw new NotFoundException("Firmware with the name "+name+" doesnot exist");
		}
	}

	@Override
	public Optional<Firmware> findByName(String firmwareName) {
		return repository.findByName(firmwareName);
	}

}
