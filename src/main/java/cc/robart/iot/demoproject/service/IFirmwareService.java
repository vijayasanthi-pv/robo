package cc.robart.iot.demoproject.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import cc.robart.iot.demoproject.persistent.Firmware;

public interface IFirmwareService {

	List<Firmware> list();

	Firmware add(@Valid Firmware firmware);

	void delete(String name);

	Firmware update(String name, Firmware firmware);

	Optional<Firmware> findByName(String firmwareName);

}
