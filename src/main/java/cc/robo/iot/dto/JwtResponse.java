package cc.robo.iot.dto;

import java.io.Serializable;

public class JwtResponse implements Serializable{

	private static final long serialVersionUID = 6992473467145475251L;
	
	private final String jwttoken;
	public JwtResponse(String jwttoken) {
		this.jwttoken = jwttoken;
	}
	public String getToken() {
		return this.jwttoken;
	}

}
