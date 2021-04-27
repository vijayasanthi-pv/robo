package cc.robo.iot.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.robo.iot.dto.Firmware;
import cc.robo.iot.exceptions.EntityAlreadyExistsException;
import cc.robo.iot.exceptions.NotFoundException;
import cc.robo.iot.persistent.FirmwareEntity;
import cc.robo.iot.repository.FirmwareRepository;
import cc.robo.iot.utils.DomainModelToViewConverter;

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
			FirmwareEntity entity = optional.get();
			domainModelToViewConverter.convert(firmware, entity);
			return domainModelToViewConverter.convert(repository.save(entity), Firmware.class);
		}
		throw new NotFoundException("Firmware with the name "+name+" does not exist");
	}

	@Override
	public Optional<FirmwareEntity> findByName(String firmwareName) {
		return repository.findByName(firmwareName);
	}

}
