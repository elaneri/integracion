package com.sso.springboot.callback;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginCallbackValidator {

	@PostMapping("/LoginCallbackValidator")
	@ResponseBody
	public String greetingJson(HttpServletRequest request) {
		String clb = "";
		Map<String, String[]> parameters = request.getParameterMap();
		for (String parameter : parameters.keySet()) {

			String[] values = parameters.get(parameter);
			clb +=  "["+parameter + "=" + values[0] + "]";

		}
		return clb;
	}
}
