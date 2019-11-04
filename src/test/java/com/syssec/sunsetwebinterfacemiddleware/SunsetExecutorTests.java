package com.syssec.sunsetwebinterfacemiddleware;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

import com.syssec.sunsetmiddleware.executor.SunsetExecutor;

public class SunsetExecutorTests {
	private SunsetExecutor sunsetExecutor;

	@Before
	public void setUp() {
		this.sunsetExecutor = new SunsetExecutor();
	}

	@Test
	public void testIllegalArgumentExceptionIsThrown() {
		assertThatThrownBy(() -> {
			this.sunsetExecutor.executeCommand("");
		}).isInstanceOf(IllegalArgumentException.class).hasMessageMatching("Empty input code received!");
	}

	@Test
	public void testHelloWorldCodeIsSuccessfullyExecuted() {
		String result = this.sunsetExecutor.executeCommand(this.getHelloWorldTestCode());

		assertThat(result).isNotNull().isNotEmpty().isEqualTo("Hello World");
	}

	private String getHelloWorldTestCode() {
		return "program HelloWorld {\n\tprintln(\"Hello World\");\n}";
	}

}
