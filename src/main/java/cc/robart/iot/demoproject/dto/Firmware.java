package cc.robart.iot.demoproject.dto;

import java.io.Serializable;

public class Firmware implements Serializable{

	private static final long serialVersionUID = 4839299283210425924L;

	public Firmware() {}
	
	public Firmware(String name, String data) {
		super();
		this.name = name;
		this.data = data;
	}
	
	private String name;
	
	private String data;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
