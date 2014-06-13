package de.anhquan.viem.core;

public class ApplicationError {
	
	private String errmsg;
	
	private int errno;

	public static ApplicationError OK = new ApplicationError(0, "Okay");
	public static ApplicationError UNKNOWN = new ApplicationError(-100, "Unknown error");
	public static ApplicationError INVALID_PARAMETER = new ApplicationError(-1, "Invalid ID or the ID is not specified");
	public static ApplicationError DUPLICATED_NAME = new ApplicationError(-2, "Duplicated name");
	public static ApplicationError ENTITY_NOT_FOUND = new ApplicationError(-3, "Entity not found");
	public static ApplicationError ACTION_FAILED = new ApplicationError(-4, "Action failed");
	public static final ApplicationError INVALID_ACTION = new ApplicationError(-5, "Invalid action");

	public ApplicationError(int errno, String msg) {
		this.errmsg = msg;
		this.setErrorNumber(errno);
	}

	public String getErrorMessage() {
		return errmsg;
	}

	public void setErrorMessage(String errmsg) {
		this.errmsg = errmsg;
	}

	public int getErrorNumber() {
		return errno;
	}

	public void setErrorNumber(int errno) {
		this.errno = errno;
	}

	public boolean isOk() {
		return errno>=0;
	}
	
	public boolean isFailed() {
		return errno<0;
	}

}
