package de.anhquan.viem.core.model;

import de.anhquan.viem.core.ApplicationError;

public class ApplicationException extends Exception{

	private static final long serialVersionUID = 1L;
	private ApplicationError error;
	
	public ApplicationException(ApplicationError error){
		this.setError(error);
	}
	
	public ApplicationException(ApplicationError error, Throwable cause){
		super(cause);
		this.setError(error);
	}
	
	public ApplicationException(ApplicationError error, String msg){
		super(msg);
		this.setError(error);
	}
	
	public ApplicationException(ApplicationError error, String msg, Throwable cause){
		super(msg, cause);
		this.setError(error);
	}

	public ApplicationError getError() {
		return error;
	}

	public void setError(ApplicationError error) {
		this.error = error;
	}
	
}
