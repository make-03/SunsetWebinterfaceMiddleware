package com.syssec.sunsetmiddleware.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Custom Error Controller to display different HTML-pages for the user
 * depending on the error instead of the default whitelabel error page.
 * 
 * @author Markus R.
 *
 */
@Controller
public class CustomErrorController implements ErrorController {

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (status != null) {
			Integer statusCode = Integer.valueOf(status.toString());

			if (statusCode == HttpStatus.BAD_REQUEST.value()) {
				return "error400";
			}else if (statusCode == HttpStatus.NOT_FOUND.value()) {
				return "error404";
			} else if(statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
				return "error405";
			} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				return "error500";
			}
		}
		return "error";
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}
