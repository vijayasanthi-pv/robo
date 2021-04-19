package cc.robart.iot.demoproject.exceptions;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Global Exception Handler
 *
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handleMethodArgNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
		ApiError error = new ApiError(400, ex.getMessage(), request.getServletPath());
		BindingResult bindingResult = ex.getBindingResult();
		Map<String, String> validationErrors = new HashMap<>();
		for(FieldError fieldError: bindingResult.getFieldErrors()) {
			validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		return error;
	}

}
