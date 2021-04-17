package cc.robart.iot.demoproject.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class JwtRequest implements Serializable{
	
	private static final long serialVersionUID = -4137175546868825265L;

	public JwtRequest() {}
	
	public JwtRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	@NotNull
	@NotEmpty
	private String username;
	
	@NotNull
	@NotEmpty
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
