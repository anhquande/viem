package de.anhquan.viem.core.json;

public class ParseJSONException extends Exception {

	private static final long serialVersionUID = 1L;

	public ParseJSONException(){
		super();
	}
	
	public ParseJSONException(String msg){
		super(msg);
	}
	
	public ParseJSONException(Throwable cause){
		super(cause);
	}
	
	public ParseJSONException(String msg, Throwable cause){
		super(msg, cause);
	}
}
