package com.hcl.retailbanking.util;

public interface AccountComposer<I, O> {
	
	O compose(I entity);
}
