package com.hcl.retailbanking.exception;

import javax.persistence.Transient;

public class CommonException extends Exception {
	private static final long serialVersionUID = 1L;
	
	@Transient
	private  MessageCode exception;

	public CommonException() {
		exception = MessageCode.RECORD_NOT_FOUND;
	}

	public MessageCode getException() {
		return exception;
	}

	public void setEx(MessageCode exception) {
		this.exception = exception;
	}

	public CommonException(MessageCode exception) {
		this.exception = exception;

	}

}
