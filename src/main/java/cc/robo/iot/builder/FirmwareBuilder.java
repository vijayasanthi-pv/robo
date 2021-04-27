package cc.robo.iot.builder;

import cc.robo.iot.dto.Firmware;
import lombok.Getter;

@Getter
public class FirmwareBuilder {
	
	private String name;
	private String data;
	
	public static FirmwareBuilder builder() {
		return new FirmwareBuilder();
	}
	
	public FirmwareBuilder name(String name) {
		this.name = name;
		return this;
	}
	public FirmwareBuilder data(String data) {
		this.data = data;
		return this;
	}
	
	public Firmware build() {
        Firmware firmware = new Firmware();
        firmware.setName(this.name);
        firmware.setData(this.data);
        return firmware;
	}
}
