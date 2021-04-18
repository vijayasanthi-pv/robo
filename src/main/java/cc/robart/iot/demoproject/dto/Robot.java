package cc.robart.iot.demoproject.dto;

import java.io.Serializable;

public class Robot implements Serializable{
	
private static final long serialVersionUID = 8609074210468663345L;
	
	public Robot() {}

	public Robot(String name, Firmware firmware) {
		super();
		this.name = name;
		this.firmware = firmware;
	}

	private String name;
	private Firmware firmware;
	
	public Firmware getFirmware() {
		return firmware;
	}

	public void setFirmware(Firmware firmware) {
		this.firmware = firmware;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
