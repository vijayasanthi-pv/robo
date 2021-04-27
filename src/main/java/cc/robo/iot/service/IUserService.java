package cc.robo.iot.service;

import cc.robo.iot.dto.User;

/**
 * Interface for User Service
 *
 */
public interface IUserService {
	void registerUser(User user);
	String login(String username, String password);
}
