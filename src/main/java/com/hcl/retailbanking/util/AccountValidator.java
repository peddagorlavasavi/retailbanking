package com.hcl.retailbanking.util;

public interface AccountValidator<T> {
	
	Boolean validate(T t);
}
