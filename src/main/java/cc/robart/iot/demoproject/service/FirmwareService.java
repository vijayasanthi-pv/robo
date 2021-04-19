package cc.robart.iot.demoproject.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.robart.iot.demoproject.dto.Firmware;
import cc.robart.iot.demoproject.exceptions.EntityAlreadyExistsException;
import cc.robart.iot.demoproject.exceptions.NotFoundException;
import cc.robart.iot.demoproject.persistent.FirmwareEntity;
import cc.robart.iot.demoproject.repository.FirmwareRepository;
import cc.robart.iot.demoproject.utils.DomainModelToViewConverter;

/**
 * Service Implementation for Firmware
 *
 */
@Service
public class FirmwareService implements IFirmwareService{
	
	Logger logger = LoggerFactory.getLogger(FirmwareService.class);
	
	@Autowired
	private FirmwareRepository repository;
	
	@Autowired
	private DomainModelToViewConverter domainModelToViewConverter;
	/**
	 * Lists the firmwares
	 * @return {@List<Firmware> }
	 *
	 */
	@Override
	public List<Firmware> list() {
		return repository.findAll().stream().map(firmwareEnity->domainModelToViewConverter.convert(firmwareEnity,Firmware.class)).collect(Collectors.toList());
	}
	
	/**
	 * Create the firmware
	 * @input firmware
	 * @output {@Firmware }
	 */
	@Override
	public Firmware create(@Valid Firmware firmware) {
		logger.info("Creating Firmware .........."+firmware);
		Optional<FirmwareEntity> optional = repository.findByName(firmware.getName());
		if(optional.isPresent()) {
			throw new EntityAlreadyExistsException("Firmware already exist");
		}else {
			FirmwareEntity firmwareEntity = repository.save(domainModelToViewConverter.convert(firmware, FirmwareEntity.class));
			return domainModelToViewConverter.convert(firmwareEntity, Firmware.class);
		}
	}

	/**
	 * Deletes the firmware
	 * @return {@List<Robot> }
	 *
	 */
	@Override
	public void delete(String name) {
		logger.info("Deleting Firmware ..........");
		Optional<FirmwareEntity> optional = repository.findByName(name);
		if(optional.isPresent()) {
			repository.delete(optional.get());
		}else {
			throw new NotFoundException("Firmware with the name "+name+" doesn't exist");
		}
	}

	/**
	 * Updates the firmware
	 * @input - name, firmware
	 * @return {Firmware }
	 *
	 */
	@Override
	public Firmware update(String name, Firmware firmware) {
		logger.info("Updating Firmware ..........");
		Optional<FirmwareEntity> optional = repository.findByName(name);
		if (optional.isPresent()) {
			FirmwareEntity firmwareEntity = domainModelToViewConverter.convert(firmware, FirmwareEntity.class);
			firmwareEntity.setId(optional.get().getId());
			return domainModelToViewConverter.convert(repository.save(firmwareEntity), Firmware.class);
		}else {
			throw new NotFoundException("Firmware with the name "+name+" doesnot exist");
		}
	}

	@Override
	public Optional<FirmwareEntity> findByName(String firmwareName) {
		return repository.findByName(firmwareName);
	}

}
