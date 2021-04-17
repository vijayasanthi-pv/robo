package cc.robart.iot.demoproject.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cc.robart.iot.demoproject.dto.Firmware;
import cc.robart.iot.demoproject.service.IFirmwareService;


@RestController
@RequestMapping("/api/firmware")
@CrossOrigin
@Validated
public class FirmwareController {
	
	@Autowired
	private IFirmwareService service;
	
	@GetMapping(value="/",produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Firmware> list() {
		List<Firmware> firmwares = service.list();	
		return firmwares;
	}
	
	@PostMapping(value="/",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public Firmware create(@Valid @RequestBody Firmware firmware) {
		return service.create(firmware);
	}
	
	@DeleteMapping(value="/{name}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable String name) {
		service.delete(name);
	}
	
	@PutMapping(value="/{name}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Firmware update(@PathVariable String name, @Valid @RequestBody Firmware firmware) {
		return service.update(name,firmware);
	}
	
}
