package com.littcore.codegen.common;



public abstract class Retry implements RetryCallback
{
	public static final int RETRY_LIMITS = 3;
	

	/**
	 * 重试次数.
	 */
	private int retryTimes;
	
	public Object doInRetry() throws RetryableException
	{
		if(RETRY_LIMITS>0 && this.getRetryTimes()>=RETRY_LIMITS)	//超过重试次数
		{
			System.out.println("超过重试次数，无法正常运行，系统退出...");
			System.exit(0);
		}
		retryTimes++;
		return null;
	}

	/**
	 * @return the retryTimes.
	 */
	public int getRetryTimes()
	{
		return retryTimes;
	}
}
