package cc.robart.iot.demoproject.exceptions;

import java.io.Serializable;

public class EntityAlreadyExistsException extends RuntimeException implements Serializable{
	
	private static final long serialVersionUID = 8564712536260076185L;

	public EntityAlreadyExistsException() {
		super();
	}

	public EntityAlreadyExistsException(String arg0) {
		super(arg0);
	}

}	
