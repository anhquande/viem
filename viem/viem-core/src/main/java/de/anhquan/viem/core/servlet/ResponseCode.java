package de.anhquan.viem.core.servlet;

class ResponseCode{

	public static final ResponseCode NOT_IMPLEMENTED = new ResponseCode(-1000,"Not Implemented");
	public static final ResponseCode UNKNOWN = new ResponseCode(-1,"Unknown error");
	public static final ResponseCode OK = new ResponseCode(0,"Success");
	
	private int value;
	private String message;

	public ResponseCode(int no, String msg){
		this.value = no;
		this.message = msg;
	}
	
	public int value(){
		return value;
	}
	
	public String message(){
		return message;
	}
	
	public boolean isSuccess(){
		return value >= 0;
	}
	
	public boolean isFailure(){
		return value < 0;
	}
	
}