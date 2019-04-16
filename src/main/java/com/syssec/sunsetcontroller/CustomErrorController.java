package com.syssec.sunsetcontroller;

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

	/**
	 * Method for handling (HTTP)errors that can occur. For this project only
	 * "general error", "error 404 - page not found" and "error 500 - internal
	 * server errror" are defined.
	 * 
	 * @param request
	 * @return name of the template to be loaded
	 */
	@RequestMapping("/error")
	public String handleError(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (status != null) {
			Integer statusCode = Integer.valueOf(status.toString());

			if (statusCode == HttpStatus.NOT_FOUND.value()) {
				return "error404";
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
