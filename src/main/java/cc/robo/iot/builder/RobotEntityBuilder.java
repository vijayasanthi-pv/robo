package cc.robo.iot.builder;

import java.util.UUID;

import cc.robo.iot.persistent.FirmwareEntity;
import cc.robo.iot.persistent.RobotEntity;
import lombok.Getter;

@Getter
public class RobotEntityBuilder {

	private UUID id;
	private String name;
	private FirmwareEntity firmwareEntity;
	
	public static RobotEntityBuilder builder() {
		return new RobotEntityBuilder();
	}
	
	public RobotEntityBuilder id(UUID id) {
		this.id = id;
		return this;
	}
	public RobotEntityBuilder name(String name) {
		this.name = name;
		return this;
	}
	public RobotEntityBuilder firmwareEntity(FirmwareEntity firmwareEntity) {
		this.firmwareEntity = firmwareEntity;
		return this;
	}
	
	public RobotEntity build() {
		RobotEntity robotEntity = new RobotEntity();
		robotEntity.setId(this.id);
		robotEntity.setName(this.name);
		robotEntity.setFirmware(this.firmwareEntity);
		return robotEntity;
	}
	
}
