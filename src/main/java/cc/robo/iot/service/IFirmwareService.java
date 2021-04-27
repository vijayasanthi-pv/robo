package cc.robo.iot.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import cc.robo.iot.dto.Firmware;
import cc.robo.iot.persistent.FirmwareEntity;

/**
 * Interface for Firmware
 *
 */
public interface IFirmwareService {

	List<Firmware> list();

	void delete(String name);

	Firmware update(String name, @Valid Firmware firmware);

	Optional<FirmwareEntity> findByName(String firmwareName);

	Firmware create(@Valid Firmware firmware);

}
