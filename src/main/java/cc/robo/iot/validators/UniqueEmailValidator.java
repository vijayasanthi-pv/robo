package cc.robo.iot.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import cc.robo.iot.repository.UserRepository;

/**
 * Custom Email validator
 */
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String>{

	@Autowired
	UserRepository userRepository;
	
	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		return !userRepository.findByEmail(email).isPresent();
	}

}
