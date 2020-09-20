package com.sso.springboot.Messages;



public enum JWTMessages {
	
	CLAIM_AGREGADA(0, "Se agrego el claim al usuario"), JWT_VALIDO(0, "El token es valido");

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
	
	
}