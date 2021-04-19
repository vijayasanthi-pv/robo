package cc.robart.iot.demoproject.service;

import java.util.ArrayList;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cc.robart.iot.demoproject.dto.User;
import cc.robart.iot.demoproject.exceptions.NotFoundException;
import cc.robart.iot.demoproject.persistent.UserEntity;
import cc.robart.iot.demoproject.repository.UserRepository;
import cc.robart.iot.demoproject.utils.DomainModelToViewConverter;
import cc.robart.iot.demoproject.utils.JwtTokenUtil;

/**
 * Service for the user
 * 
 */
@Service
public class UserService implements IUserService, UserDetailsService{

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserDetailsService userService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private DomainModelToViewConverter domainModelToViewConverter;

	/**
	 * Registers user in the database
	 *
	 */
	@Override
	public void registerUser(User user) {
		String encryptedPwd = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		user.setPassword(encryptedPwd);
		userRepository.save(domainModelToViewConverter.convert(user, UserEntity.class));
	}

	/**
	 * Login the user 
	 * input name, password
	 */
	@Override
	public String login(String name, String password) {

		String token = null;
		Optional<UserEntity> optional = userRepository.findByName(name);
		
		if (!optional.isPresent())
			throw new NotFoundException("User "+name+"doesn't exist");
		
		UserEntity userEntity = optional.get();
		String encryptedPassword = userEntity.getPassword();

		if (checkPass(password, encryptedPassword)) {
			try {
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(name, encryptedPassword));
				final UserDetails userDetails = userService.loadUserByUsername(name);
				token = jwtTokenUtil.generateToken(userDetails);
			}catch(BadCredentialsException e) {
				throw new BadCredentialsException("Incorrect username or password", e);
			}
		}

		return token;
	}

	/**
	 * Verifies the password with encrypted
	 *
	 */
	private boolean checkPass(String plainPassword, String hashedPassword) {
		boolean pwdValidation = false;
		if (BCrypt.checkpw(plainPassword, hashedPassword))
			pwdValidation = true;
		
		return pwdValidation;
	}

	/**
	 * Loads the user by name
	 *
	 */
	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		Optional<UserEntity> optional = userRepository.findByName(name);
		if (!optional.isPresent())
			throw new NotFoundException("User "+name+"doesn't exist");
		UserEntity userEntity = optional.get();
		
		return new org.springframework.security.core.userdetails.User(userEntity.getName(), userEntity.getPassword(), new ArrayList<>());
	}

}
