package cc.robart.iot.demoproject.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cc.robart.iot.demoproject.dto.Firmware;
import cc.robart.iot.demoproject.dto.Robot;
import cc.robart.iot.demoproject.service.IRobotService;

@RestController
@RequestMapping("/api/robot")
@CrossOrigin
@Validated
public class RobotController {
	
	@Autowired
	private IRobotService service;

	@GetMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Robot> list() {
		return service.list();
	}
	
	@PostMapping(path="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public Robot create(@Valid @RequestBody Robot robot) {
		return service.create(robot);
	}
	
	@PutMapping(value="/assignFirmware/{firmwareName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public void assignFirmware(@PathVariable String firmwareName, @RequestBody List<String> robotNames) {
		service.assignFirmware(firmwareName, robotNames);
	}
	
	@GetMapping(path="/{name}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Firmware latestFirmware(@PathVariable String name) {
		return service.latestFirmware(name);
	}
}
