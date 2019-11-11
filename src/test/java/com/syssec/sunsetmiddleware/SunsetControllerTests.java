package com.syssec.sunsetmiddleware;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import com.syssec.sunsetmiddleware.controller.SunsetController;
import com.syssec.sunsetmiddleware.messages.SunsetGlobalMessages;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { SunsetController.class })
public class SunsetControllerTests {
	private final Logger logger = Logger.getLogger(SunsetControllerTests.class);
	private final int TIMEOUT_SECONDS = 5;

	private SunsetController sunsetController;
	private MockMvc mockMvc;
	private String id;

	@Before
	public void setUp() {
		this.sunsetController = new SunsetController();
		sunsetController.getSunsetThreadPool().getThreadPoolTaskExecutor().setKeepAliveSeconds(this.TIMEOUT_SECONDS);
		sunsetController.getSunsetThreadPool().getThreadPoolTaskExecutor().initialize();

		this.mockMvc = MockMvcBuilders.standaloneSetup(this.sunsetController).apply(sharedHttpSession()).build();

		this.id = UUID.randomUUID().toString();
	}

	@After
	public void tearDown() {
		this.sunsetController.getSunsetThreadPool().shutdownExecutorService();
	}

	@Test
	public void testControllerReceivingEmptyCodeThrowsIllegalArgumentException() {
		assertThatThrownBy(() -> {
			this.sunsetController.executeCode("", UUID.randomUUID().toString());
		}).isInstanceOf(IllegalArgumentException.class).hasMessageMatching(SunsetGlobalMessages.EMPTY_CODE_RECEIVED);
	}

	@Test
	public void testHttpPostRequestWithValidParametersReturnsStatusCodeIsOk() throws Exception {
		this.mockMvc.perform(post("/result").param("code", "testCode").param("uniqueId", this.id))
				.andExpect(status().isOk());
	}

	@Test
	public void testHttpPostRequestWithEmptyCodeParameterThrowsIllegalArgumentException() {
		assertThatThrownBy(() -> {
			this.mockMvc.perform(post("/result").param("code", "").param("uniqueId", this.id));
		}).isInstanceOf(NestedServletException.class).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining(SunsetGlobalMessages.EMPTY_CODE_RECEIVED);
	}

	@Test
	public void testHttpPostRequestWithValidButSyntacticallyIncorrectCodeParameterReturnsFFaplParseExceptionString()
			throws Exception {
		String code = "testCode";
		MvcResult result = this.mockMvc.perform(post("/result").param("code", code).param("uniqueId", this.id))
				.andExpect(status().isOk()).andReturn();
		String codeAfterRequest = result.getModelAndView().getModelMap().get("codeOriginal").toString();
		String resultString = result.getModelAndView().getModelMap().get("codeResult").toString();

		assertThat(codeAfterRequest).isNotNull().isNotEmpty().isEqualTo(code);
		assertThat(resultString).isNotNull().isNotEmpty()
				.isEqualTo("FFapl Kompilierung: [] ParseException 102 (Zeile 1, Spalte 1)" + "\n \"" + code
						+ "\" erkannt in Zeile 1, Spalte 1. Erwartete jedoch: \"program\" ...");
	}

	@Test
	public void testHttpPostRequestWithCorrectParametersReturnsExpectedResult() throws Exception {
		MvcResult result = this.mockMvc
				.perform(post("/result").param("code", this.getHelloWorldTestCode()).param("uniqueId", this.id))
				.andExpect(status().isOk()).andReturn();
		String codeAfterRequest = result.getModelAndView().getModelMap().get("codeOriginal").toString();
		String resultString = result.getModelAndView().getModelMap().get("codeResult").toString();

		assertThat(codeAfterRequest).isNotNull().isNotEmpty().isEqualTo(this.getHelloWorldTestCode());
		assertThat(resultString).isNotNull().isNotEmpty().isEqualTo("Hello World");
	}

	private String getHelloWorldTestCode() {
		return "program HelloWorld {\n\tprintln(\"Hello World\");\n}";
	}

	@Test
	public void testHttpPostRequstWithoutParametersReturnsStatusCodeBadRequest() throws Exception {
		this.mockMvc.perform(post("/result")).andExpect(status().isBadRequest());
		this.mockMvc.perform(post("/cancelled")).andExpect(status().isBadRequest());
	}

	@Test
	public void testHttpOptionsRequestIsAllowed() throws Exception {
		this.mockMvc.perform(options("/result")).andExpect(status().isOk());
		this.mockMvc.perform(options("/cancelled")).andExpect(status().isOk());
	}

	@Test
	public void testHttpOptionsRequestReturnsAllowHeaderWithOnlyPost() throws Exception {
		MvcResult mvcResult = this.mockMvc.perform(options("/result")).andReturn();
		assertThat(mvcResult.getResponse().getHeader("Allow")).isEqualTo("POST");

		MvcResult mvcCancelled = this.mockMvc.perform(options("/cancelled")).andReturn();
		assertThat(mvcCancelled.getResponse().getHeader("Allow")).isEqualTo("POST");
	}

	@Test
	public void testHttpHeadRequestIsNotAllowed() throws Exception {
		this.mockMvc.perform(head("/result")).andExpect(status().isMethodNotAllowed());
		this.mockMvc.perform(head("/cancelled")).andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void testHttpGetRequestIsNotAllowed() throws Exception {
		this.mockMvc.perform(get("/result")).andExpect(status().isMethodNotAllowed());
		this.mockMvc.perform(get("/cancelled")).andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void testHttpPutRequestIsNotAllowed() throws Exception {
		this.mockMvc.perform(put("/result")).andExpect(status().isMethodNotAllowed());
		this.mockMvc.perform(put("/cancelled")).andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void testHttpDeleteRequestIsNotAllowed() throws Exception {
		this.mockMvc.perform(delete("/result")).andExpect(status().isMethodNotAllowed());
		this.mockMvc.perform(delete("/cancelled")).andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void testTimeoutOfExecutionReturnsCorrectResult() throws Exception {
		logger.debug("[Test] Executing sunset code with endless loop using a timeout of " + this.TIMEOUT_SECONDS
				+ " seconds ...");
		MvcResult result = this.mockMvc
				.perform(post("/result").param("code", this.getEndlessLoopTestCode()).param("uniqueId", this.id))
				.andExpect(status().isOk()).andReturn();
		String codeAfterRequest = result.getModelAndView().getModelMap().get("codeOriginal").toString();
		String resultString = result.getModelAndView().getModelMap().get("codeResult").toString();

		assertThat(codeAfterRequest).isNotNull().isNotEmpty().isEqualTo(this.getEndlessLoopTestCode());
		assertThat(resultString).isNotNull().isNotEmpty()
				.isEqualTo(String.format(SunsetGlobalMessages.TIMEOUT_EXCEPTION, this.TIMEOUT_SECONDS));
	}

	@Test
	public void testCancellingRunningExecutionOfCodeIsSuccessfull() throws Exception {
		this.threadExecuteCodeEndlessLoop.start();
		Thread.sleep(50);
		this.threadCancelExecutionOfEndlessLoop.start();

		this.threadExecuteCodeEndlessLoop.join();
		this.threadCancelExecutionOfEndlessLoop.join();

		assertThat(this.resultOfCancelledCalculation).isNotNull().isNotEmpty()
				.isEqualTo("Execution was cancelled by the user!");
	}

	private Thread threadExecuteCodeEndlessLoop = new Thread() {
		public void run() {
			try {
				mockMvc.perform(post("/result").param("code", getEndlessLoopTestCode()).param("uniqueId", id));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private String getEndlessLoopTestCode() {
		return "program Endless { \n\twhile(true) {}\n}";
	}

	private String resultOfCancelledCalculation = "";
	private Thread threadCancelExecutionOfEndlessLoop = new Thread() {
		public void run() {
			try {
				MvcResult result = mockMvc.perform(post("/cancelled").param("uniqueId2", id)).andReturn();
				resultOfCancelledCalculation = result.getModelAndView().getModelMap().get("codeResult").toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Test
	public void testAbortPolicyOfThreadPoolReturnsServerIsOverloadedStringIfThreadPoolAndQueueAreFull()
			throws InterruptedException {
		final int CORE_POOL_SIZE_TEST = 1;
		final int MAX_POOL_SIZE_TEST = 1;
		final int QUEUE_CAPACITY_TEST = 1;

		this.sunsetController.getSunsetThreadPool().getThreadPoolTaskExecutor().setCorePoolSize(CORE_POOL_SIZE_TEST);
		this.sunsetController.getSunsetThreadPool().getThreadPoolTaskExecutor().setMaxPoolSize(MAX_POOL_SIZE_TEST);
		this.sunsetController.getSunsetThreadPool().getThreadPoolTaskExecutor().setQueueCapacity(QUEUE_CAPACITY_TEST);
		this.sunsetController.getSunsetThreadPool().getThreadPoolTaskExecutor().initialize();

		this.threadExecuteCodeHelloWorld1.start();
		Thread.sleep(50);
		this.threadExecuteCodeHelloWorld2.start();
		Thread.sleep(50);
		this.threadExecuteCodeHelloWorld3CausingTaskRejectionException.start();

		this.threadExecuteCodeHelloWorld1.join();
		this.threadExecuteCodeHelloWorld2.join();
		this.threadExecuteCodeHelloWorld3CausingTaskRejectionException.join();

		assertThat(this.resultThread1).isNotNull().isNotEmpty().isEqualTo("Hello World");
		assertThat(this.resultThread2).isNotNull().isNotEmpty().isEqualTo("Hello World");
		assertThat(this.resultThread3).isNotNull().isNotEmpty().isEqualTo(SunsetGlobalMessages.SERVER_IS_OVERLOADED);
	}

	private String resultThread1 = "";
	private Thread threadExecuteCodeHelloWorld1 = new Thread() {
		public void run() {
			try {
				MvcResult result = mockMvc.perform(post("/result").param("code", getHelloWorldTestCode())
						.param("uniqueId", UUID.randomUUID().toString())).andExpect(status().isOk()).andReturn();
				resultThread1 = result.getModelAndView().getModel().get("codeResult").toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private String resultThread2 = "";
	private Thread threadExecuteCodeHelloWorld2 = new Thread() {
		public void run() {
			try {
				MvcResult result = mockMvc.perform(post("/result").param("code", getHelloWorldTestCode())
						.param("uniqueId", UUID.randomUUID().toString())).andExpect(status().isOk()).andReturn();
				resultThread2 = result.getModelAndView().getModel().get("codeResult").toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private String resultThread3 = "";
	private Thread threadExecuteCodeHelloWorld3CausingTaskRejectionException = new Thread() {
		public void run() {
			try {
				MvcResult result = mockMvc.perform(post("/result").param("code", getHelloWorldTestCode())
						.param("uniqueId", UUID.randomUUID().toString())).andExpect(status().isOk()).andReturn();
				resultThread3 = result.getModelAndView().getModel().get("codeResult").toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

}
