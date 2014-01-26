package com.litt.core.codegen.common;


public interface RetryCallback
{
	public Object doInRetry() throws RetryableException;
}
