package cc.robo.iot.builder;

import java.util.UUID;

import cc.robo.iot.persistent.FirmwareEntity;
import lombok.Getter;

@Getter
public class FirmwareEntityBuilder {

	private UUID id;
	private String name;
	private String data;
	
	public static FirmwareEntityBuilder builder() {
		return new FirmwareEntityBuilder();
	}
	
	public FirmwareEntityBuilder id(UUID id) {
		this.id = id;
		return this;
	}
	
	public FirmwareEntityBuilder name(String name) {
		this.name = name;
		return this;
	}
	public FirmwareEntityBuilder data(String data) {
		this.data = data;
		return this;
	}
	
	public FirmwareEntity build() {
        FirmwareEntity firmwareEntity = new FirmwareEntity();
        firmwareEntity.setId(this.id);
        firmwareEntity.setName(this.name);
        firmwareEntity.setData(this.data);
        return firmwareEntity;
	}

}
