package cc.robart.iot.demoproject.service;

import java.util.List;

import javax.validation.Valid;

import cc.robart.iot.demoproject.persistent.Firmware;

public interface IFirmwareService {

	List<Firmware> list();

	Firmware add(@Valid Firmware firmware);

	void delete(String name);

	Firmware update(String name, Firmware firmware);

	void assignFirmware(String name, List<String> robotNames);

}
