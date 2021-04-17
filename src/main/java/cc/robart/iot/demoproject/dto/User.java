package cc.robart.iot.demoproject.dto;

import java.io.Serializable;

public class User implements Serializable{
	
	private static final long serialVersionUID = -4927470250156446772L;

	public User() {}

	public User(String name, String password, String email) {
		super();
		this.name = name;
		this.password = password;
		this.email = email;
	}

	private String name;
	private String password;
	private String email;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
