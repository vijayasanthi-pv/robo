package cc.robart.iot.demoproject.dto;

import java.io.Serializable;

public class Robot implements Serializable{

	private static final long serialVersionUID = 8609074210468663345L;
	
	private String name;
	private Firmware firmware;

	public Robot() {}

	public Robot(String name, Firmware firmware) {
		super();
		this.name = name;
		this.firmware = firmware;
	}

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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firmware == null) ? 0 : firmware.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Robot other = (Robot) obj;
		if (firmware == null) {
			if (other.firmware != null)
				return false;
		} else if (!firmware.equals(other.firmware))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Robot [name=" + name + ", firmware=" + firmware + "]";
	}


}
