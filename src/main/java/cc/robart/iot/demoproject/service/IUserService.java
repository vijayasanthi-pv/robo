package cc.robart.iot.demoproject.service;

import cc.robart.iot.demoproject.dto.User;

public interface IUserService {
	void registerUser(User user);
	String login(String username, String password);
}
