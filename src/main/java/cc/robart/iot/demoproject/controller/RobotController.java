package cc.robart.iot.demoproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cc.robart.iot.demoproject.persistent.Firmware;
import cc.robart.iot.demoproject.persistent.Robot;
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
	
	@GetMapping(path="/{name}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Firmware latestFirmware(@RequestParam String name) {
		return service.latestFirmware(name);
	}
}
