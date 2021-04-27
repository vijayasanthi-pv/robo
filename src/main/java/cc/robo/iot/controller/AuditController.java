package cc.robo.iot.controller;

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

import cc.robo.iot.persistent.FirmwareEntity;
import cc.robo.iot.persistent.RobotEntity;
import cc.robo.iot.repository.FirmwareRepository;
import cc.robo.iot.repository.RobotRepository;

/**
 * Audit controller to track the changes of robot and firmware
 *
 */
@RestController
@RequestMapping("/api/audit")
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
	
	/**
	 * Returns the changes for a particular robot
	 * @input id - UUID of the robot
	 * @output - pretty print of the changes
	 */
	@GetMapping("/robot/{id}")
    public String getParticularRobotChanges(@PathVariable UUID id) {
        QueryBuilder jqlQuery = QueryBuilder.byInstanceId(id, RobotEntity.class)
                .withNewObjectChanges();

        Changes changes = javers.findChanges(jqlQuery.build());

        return "<pre>" + changes.prettyPrint() + "</pre>";
    }
	
	/**
	 * Returns the firmwares changes
	 * @output - pretty print of the changes
	 */
	@GetMapping("/firmwares")
    public String getFirmwareChanges() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(FirmwareEntity.class)
                .withNewObjectChanges();

        Changes changes = javers.findChanges(jqlQuery.build());

        return "<pre>" + changes.prettyPrint() + "</pre>";
    }
}
