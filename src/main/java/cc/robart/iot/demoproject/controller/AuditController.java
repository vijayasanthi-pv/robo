package cc.robart.iot.demoproject.controller;

import java.util.UUID;

import org.javers.core.Changes;
import org.javers.core.Javers;
import org.javers.repository.jql.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.robart.iot.demoproject.persistent.Firmware;
import cc.robart.iot.demoproject.persistent.Robot;
import cc.robart.iot.demoproject.repository.FirmwareRepository;
import cc.robart.iot.demoproject.repository.RobotRepository;

@RestController
@RequestMapping("/api/audit")
@CrossOrigin
public class AuditController {

	private final Javers javers;
	private final RobotRepository robotRepository;
	private final FirmwareRepository firmwareRepository;

	@Autowired
	public AuditController(Javers javers, RobotRepository robotRepository, FirmwareRepository firmwareRepository) {
		this.javers = javers;
		this.robotRepository = robotRepository;
		this.firmwareRepository = firmwareRepository;
	}
	
	@GetMapping("/robot/{id}")
    public String getPersonChanges(@PathVariable UUID id) {
        QueryBuilder jqlQuery = QueryBuilder.byInstanceId(id, Robot.class)
                .withNewObjectChanges();

        Changes changes = javers.findChanges(jqlQuery.build());

        return "<pre>" + changes.prettyPrint() + "</pre>";
    }
	
	@RequestMapping("/firmwares")
    public String getPersonChanges() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(Firmware.class)
                .withNewObjectChanges();

        Changes changes = javers.findChanges(jqlQuery.build());

        return "<pre>" + changes.prettyPrint() + "</pre>";
    }
}
