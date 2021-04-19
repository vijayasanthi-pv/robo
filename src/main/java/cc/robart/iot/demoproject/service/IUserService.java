package cc.robart.iot.demoproject.service;

import cc.robart.iot.demoproject.dto.User;

/**
 * Interface for User Service
 *
 */
public interface IUserService {
	void registerUser(User user);
	String login(String username, String password);
}
