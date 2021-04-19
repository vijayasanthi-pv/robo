package cc.robart.iot.demoproject.controller;

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

import cc.robart.iot.demoproject.dto.JwtRequest;
import cc.robart.iot.demoproject.dto.JwtResponse;
import cc.robart.iot.demoproject.dto.User;
import cc.robart.iot.demoproject.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Controller for the user
 *
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin
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
