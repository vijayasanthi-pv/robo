package cc.robo.iot.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cc.robo.iot.dto.JwtRequest;
import cc.robo.iot.dto.JwtResponse;
import cc.robo.iot.dto.User;
import cc.robo.iot.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Controller for the user
 *
 */
@RestController
@RequestMapping("/api/user")
@Api( tags = "Users")
@Validated
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	@ApiOperation(value = "This method is used to register users.")
	@PostMapping(value="/register")
	@ResponseStatus(HttpStatus.CREATED)
	public void registerUser(@Valid @RequestBody User user) {
		userService.registerUser(user);
	}
	
	@PostMapping(value="/login")
	public ResponseEntity<?> login(@Valid @RequestBody JwtRequest user) {
		
		final String token =  userService.login(user.getUsername(), user.getPassword());
		return ResponseEntity.ok(new JwtResponse(token));
	}

}
