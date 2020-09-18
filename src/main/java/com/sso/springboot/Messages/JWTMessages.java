package com.sso.springboot.Messages;

import org.json.JSONException;
import org.json.JSONObject;

public enum JWTMessages {
	
	CLAIM_AGREGADA(0, "Se agrego el claim al usuario");

	private final int code;
	private final String description;

	private JWTMessages(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
			return "{\"MSG\":\""+description+"\"}";
	}
	
	public JSONObject toJson() throws JSONException {
			return new JSONObject(this.toString());
	}
}