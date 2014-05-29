package de.anhquan.viem.core.util;

public class ParseSettingException extends Exception {

	private static final long serialVersionUID = 1L;

	public ParseSettingException(){
		super();
	}
	
	public ParseSettingException(String msg){
		super(msg);
	}
	
	public ParseSettingException(Throwable cause){
		super(cause);
	}
	
	public ParseSettingException(String msg, Throwable cause){
		super(msg, cause);
	}
}
