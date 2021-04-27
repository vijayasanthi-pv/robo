package cc.robo.iot.builder;

import cc.robo.iot.dto.Firmware;
import cc.robo.iot.dto.Robot;
import lombok.Getter;

@Getter
public class RobotBuilder {
	
	private String name;
	private Firmware firmware;
	
	public static RobotBuilder builder() {
		return new RobotBuilder();
	}
	
	public RobotBuilder name(String name) {
		this.name = name;
		return this;
	}

	public RobotBuilder firmware(Firmware firmware) {
		this.firmware = firmware;
		return this;
	}

	public Robot build() {
		Robot robot = new Robot();
		robot.setName(this.name);
		robot.setFirmware(this.firmware);
		return robot;
	}
	
}
