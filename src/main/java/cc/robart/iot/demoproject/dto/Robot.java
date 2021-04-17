package cc.robart.iot.demoproject.dto;

import java.io.Serializable;

public class Robot implements Serializable{
	
private static final long serialVersionUID = 8609074210468663345L;
	
	public Robot() {}

	public Robot(String name, String firmwareName, String firmwareData) {
		super();
		this.name = name;
		this.firmwareName = firmwareName;
		this.firmwareData = firmwareData;
	}

	private String name;
	private String firmwareName;
	private String firmwareData;
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

	public String getFirmwareName() {
		return firmwareName;
	}

	public void setFirmwareName(String firmwareName) {
		this.firmwareName = firmwareName;
	}

	public String getFirmwareData() {
		return firmwareData;
	}

	public void setFirmwareData(String firmwareData) {
		this.firmwareData = firmwareData;
	}

}
