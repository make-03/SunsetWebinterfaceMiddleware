package com.syssec.sunsetmiddleware;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

import com.syssec.sunsetmiddleware.executor.SunsetExecutor;
import com.syssec.sunsetmiddleware.messages.SunsetGlobalMessages;

public class SunsetExecutorTests {
	private final int TIMEOUT_SECONDS = 5;
	private SunsetExecutor sunsetExecutor;

	@Before
	public void setUp() {
		this.sunsetExecutor = new SunsetExecutor();
		this.sunsetExecutor.setTimeoutSeconds(TIMEOUT_SECONDS);
	}

	@Test
	public void testIllegalArgumentExceptionIsThrown() {
		assertThatThrownBy(() -> {
			this.sunsetExecutor.executeCommand("");
		}).isInstanceOf(IllegalArgumentException.class).hasMessageMatching(SunsetGlobalMessages.EMPTY_CODE_RECEIVED);
	}

	@Test
	public void testHelloWorldCodeIsSuccessfullyExecuted() throws TimeoutException, InterruptedException {
		String result = this.sunsetExecutor.executeCommand(this.getHelloWorldTestCode());

		assertThat(result).isNotNull().isNotEmpty().isEqualTo("Hello World");
	}

	private String getHelloWorldTestCode() {
		return "program HelloWorld {\n\tprintln(\"Hello World\");\n}";
	}

	@Test
	public void testProcessExecutionOfEndlessLoopThrowsInterruptedException() {
		assertThatThrownBy(() -> {
			this.sunsetExecutor.executeCommand(this.getEndlessLoopTestCode());
		}).isInstanceOf(TimeoutException.class)
				.hasMessageMatching("Timeout occurred after " + TIMEOUT_SECONDS + " seconds!");
	}

	private String getEndlessLoopTestCode() {
		return "program Endless { \n\twhile(true) {}\n}";
	}

}
