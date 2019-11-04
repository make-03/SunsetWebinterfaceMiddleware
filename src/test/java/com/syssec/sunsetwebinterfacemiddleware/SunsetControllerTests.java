package com.syssec.sunsetwebinterfacemiddleware;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.NestedServletException;

import com.syssec.sunsetmiddleware.controller.SunsetController;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { SunsetController.class })
public class SunsetControllerTests {
	private SunsetController sunsetController;
	private String id;

	@Autowired
	private MockMvc mvc;

	@Before
	public void setUp() {
		this.sunsetController = new SunsetController();
		this.id = UUID.randomUUID().toString();
	}

	@Test
	public void testIllegalArgumentExceptionIsThrown() {
		assertThatThrownBy(() -> {
			this.sunsetController.executeCode("", UUID.randomUUID().toString());
		}).isInstanceOf(IllegalArgumentException.class).hasMessageMatching("Empty input code received!");
	}

	@Test
	public void testHttpPostRequestWithValidParametersReturnsIsOk() throws Exception {
		this.mvc.perform(post("/result").param("code", "testCode").param("uniqueId", this.id))
				.andExpect(status().isOk());
	}

	@Test
	public void testHttpPostRequestWithEmptyCodeParameterThrowsException() {
		assertThatThrownBy(() -> {
			this.mvc.perform(post("/result").param("code", "").param("uniqueId", this.id));
		}).isInstanceOf(NestedServletException.class).hasMessageContaining("Empty input code received!");
	}

	@Test
	public void testHttpPostRequestWithValidButSyntacticallyIncorrectCodeParameterReturnsFFaplParseException()
			throws Exception {
		String code = "testCode";
		MvcResult result = this.mvc.perform(post("/result").param("code", code).param("uniqueId", this.id)).andReturn();
		String codeAfterRequest = result.getModelAndView().getModelMap().get("codeOriginal").toString();
		String resultString = result.getModelAndView().getModelMap().get("codeResult").toString();

		assertThat(codeAfterRequest).isNotNull().isNotEmpty().isEqualTo("testCode");
		assertThat(resultString).isNotNull().isNotEmpty()
				.isEqualTo("FFapl Kompilierung: [] ParseException 102 (Zeile 1, Spalte 1)"
						+ "\n \"testCode\" erkannt in Zeile 1, Spalte 1. Erwartete jedoch: \"program\" ...");
	}

	@Test
	public void testHttpPostRequestWithCorrectParametersReturnsExpectedResult() throws Exception {
		MvcResult result = this.mvc
				.perform(post("/result").param("code", this.getHelloWorldTestCode()).param("uniqueId", this.id))
				.andReturn();
		String codeAfterRequest = result.getModelAndView().getModelMap().get("codeOriginal").toString();
		String resultString = result.getModelAndView().getModelMap().get("codeResult").toString();

		assertThat(codeAfterRequest).isNotNull().isNotEmpty().isEqualTo(this.getHelloWorldTestCode());
		assertThat(resultString).isNotNull().isNotEmpty().isEqualTo("Hello World");
	}

	private String getHelloWorldTestCode() {
		return "program HelloWorld {\n\tprintln(\"Hello World\");\n}";
	}

	@Test
	public void testHttpHeadRequestIsNotAllowed() throws Exception {
		this.mvc.perform(head("/result")).andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void testHttpGetRequestIsNotAllowed() throws Exception {
		this.mvc.perform(get("/result")).andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void testHttpPutRequestIsNotAllowed() throws Exception {
		this.mvc.perform(put("/result")).andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void testHttpDeleteRequestIsNotAllowed() throws Exception {
		this.mvc.perform(delete("/result")).andExpect(status().isMethodNotAllowed());
	}

}
