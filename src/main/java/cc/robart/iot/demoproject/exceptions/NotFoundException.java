package cc.robart.iot.demoproject.exceptions;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom Exception class - NotFoundException class 
 *
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException implements Serializable{
	
	public NotFoundException() {
		super();
	}

	public NotFoundException(String arg0) {
		super(arg0);
	}

	private static final long serialVersionUID = -3063636599321722108L;
	
}