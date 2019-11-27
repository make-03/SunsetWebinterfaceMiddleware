package com.syssec.sunsetmiddleware.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class SunsetThreadPoolConfiguration {
	private int corepoolsize, maxpoolsize, queuecapacity, keepaliveseconds;
	private String threadnameprefix;
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	public SunsetThreadPoolConfiguration() {
		try (InputStream input = new FileInputStream("./threadpoolconfig.properties")) {
			Properties prop = new Properties();
			
			prop.load(input);
			
			this.corepoolsize = Integer.parseInt(prop.getProperty("threadpool.corepoolsize"));
			this.maxpoolsize = Integer.parseInt(prop.getProperty("threadpool.maxpoolsize"));
			this.queuecapacity = Integer.parseInt(prop.getProperty("threadpool.queuecapacity"));
			this.keepaliveseconds = Integer.parseInt(prop.getProperty("threadpool.keepaliveseconds"));
			this.threadnameprefix = prop.getProperty("threadpool.threadnameprefix");
			
			this.threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
			this.threadPoolTaskExecutor.setCorePoolSize(this.corepoolsize);
			this.threadPoolTaskExecutor.setMaxPoolSize(this.maxpoolsize);
			this.threadPoolTaskExecutor.setQueueCapacity(this.queuecapacity);
			this.threadPoolTaskExecutor.setKeepAliveSeconds(this.keepaliveseconds);
			this.threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
			this.threadPoolTaskExecutor.setThreadNamePrefix(this.threadnameprefix);
			this.threadPoolTaskExecutor.initialize();
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}

	public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
		return this.threadPoolTaskExecutor;
	}

	public void resetValuesToDefaultFromPropertyFile() {
		this.threadPoolTaskExecutor.setCorePoolSize(this.corepoolsize);
		this.threadPoolTaskExecutor.setMaxPoolSize(this.maxpoolsize);
		this.threadPoolTaskExecutor.setQueueCapacity(this.queuecapacity);
		this.threadPoolTaskExecutor.setKeepAliveSeconds(this.keepaliveseconds);
		this.threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		this.threadPoolTaskExecutor.setThreadNamePrefix(this.threadnameprefix);
		this.threadPoolTaskExecutor.initialize();
	}

	public int getCorepoolsize() {
		return corepoolsize;
	}

	public int getMaxpoolsize() {
		return maxpoolsize;
	}

	public int getQueuecapacity() {
		return queuecapacity;
	}

	public int getKeepaliveseconds() {
		return keepaliveseconds;
	}
	
}
