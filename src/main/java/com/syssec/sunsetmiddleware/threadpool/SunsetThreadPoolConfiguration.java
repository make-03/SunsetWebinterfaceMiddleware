package com.syssec.sunsetmiddleware.threadpool;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class SunsetThreadPoolConfiguration {
	private static final int CORE_POOL_SIZE_DEFAULT = 8;
	private static final int MAX_POOL_SIZE_DEFAULT = 16;
	private static final int QUEUE_CAPACITY_DEFAULT = 16;
	private static final int KEEP_ALIVE_SECONDS_DEFAULT = 10;
	
	private static ThreadPoolTaskExecutor threadPoolTaskExecutor;
	
	protected static ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
		threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(CORE_POOL_SIZE_DEFAULT);
		threadPoolTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE_DEFAULT);
		threadPoolTaskExecutor.setQueueCapacity(QUEUE_CAPACITY_DEFAULT);
		threadPoolTaskExecutor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS_DEFAULT);
		threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		threadPoolTaskExecutor.setThreadNamePrefix("SunsetThreadpoolTaskExecutor-");
		threadPoolTaskExecutor.initialize();
		
		return threadPoolTaskExecutor;
	}

	public static int getCorePoolSize() {
		return threadPoolTaskExecutor.getCorePoolSize();
	}

	public static int getMaxPoolSize() {
		return threadPoolTaskExecutor.getMaxPoolSize();
	}

	public static int getQueueCapacity() {
		return QUEUE_CAPACITY_DEFAULT;
	}
	
	public static int getKeepAliveSecondsDefaultValue() {
		return KEEP_ALIVE_SECONDS_DEFAULT;
	}

	public static int getKeepAliveSeconds() {
		return threadPoolTaskExecutor.getKeepAliveSeconds();
	}
	
	public static void setKeepAliveSeconds(int keepAliveSeconds) {
		if(keepAliveSeconds <= 0) {
			throw new IllegalArgumentException("Value for keepAliveSeconds must be >0");
		}
		
		threadPoolTaskExecutor.setKeepAliveSeconds(keepAliveSeconds);
	}
	
}
