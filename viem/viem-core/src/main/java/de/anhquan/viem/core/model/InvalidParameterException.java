package de.anhquan.viem.core.model;

import de.anhquan.viem.core.ApplicationError;

public class InvalidParameterException extends ApplicationException{

	private static final long serialVersionUID = 1L;
	
	public InvalidParameterException(String param){
		super(ApplicationError.INVALID_PARAMETER, "The requested paramater '"+param+"' is invalid.");
	}
		
}
