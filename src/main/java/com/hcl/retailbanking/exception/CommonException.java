package com.hcl.retailbanking.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommonException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	MessageCode exception;

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
