package com.hcl.retailbanking.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommonException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	MessageCode ex;

	public CommonException() {
		ex = MessageCode.RECORD_NOT_FOUND;
	}

	public MessageCode getEx() {
		return ex;
	}

	public void setEx(MessageCode ex) {
		this.ex = ex;
	}

	public CommonException(MessageCode ex) {
		this.ex = ex;

	}

}
