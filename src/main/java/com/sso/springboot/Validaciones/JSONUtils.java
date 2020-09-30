package com.sso.springboot.Validaciones;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class JSONUtils {

	public static boolean isJSONValido(String jsonString) {

		Gson gson = new Gson();
		
		try {
			gson.fromJson(jsonString, Object.class);
			return true;
		} catch (JsonSyntaxException ex) {
			return false;
		} catch (Exception ex) {
			return false;
		}
	}
}
