package com.littcore.codegen.common;


public interface RetryCallback
{
	public Object doInRetry() throws RetryableException;
}
