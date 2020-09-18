package com.sso.springboot.Error;


public enum JWTError {
	
	TENANT_API_KEY(0, "Error Tenant ApiKey no valida"),
	JWT_ERROR(1,"No se ha podido obtener el Token JWT"), 
	JWT_EXPITADO(1, "El Token JWT ha expirado");

	private final int code;
	private final String description;

	private JWTError(int code, String description) {
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
			return "{\"ERROR\":\""+description+"\"}";
	}
}