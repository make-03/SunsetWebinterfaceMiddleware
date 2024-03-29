package com.syssec.sunsetmiddleware.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuration class for configuring the ThreadPool and the underlying ThreadPoolTaskExecutor. 
 * Reads data from external properties file stored in "./threadpoolconfig.properties".
 * 
 * @author Markus R.
 *
 */
public class SunsetThreadPoolConfiguration {
	private static final Logger LOGGER = LogManager.getLogger(SunsetThreadPoolConfiguration.class);
	
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
			LOGGER.error(ex.getMessage());
			System.exit(1);
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
