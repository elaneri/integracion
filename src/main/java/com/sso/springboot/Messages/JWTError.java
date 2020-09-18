package com.sso.springboot.Messages;

import org.json.JSONException;
import org.json.JSONObject;

public enum JWTError {
	
	TENANT_API_KEY(0, "Error Tenant ApiKey no valida"),
	JWT_ERROR(1,"No se ha podido obtener el Token JWT"), 
	JWT_EXPITADO(1, "El Token JWT ha expirado"), 
	CLAIM_DUPLICADA(1, "El usuario ya posee un claim con el mismo nombre"), 
	USUARIO_NOMBRE(1, "El nombre de usuario no puede estar vacío"),
	CLAIM_NO_VALIDA(1, "El nombre de claim no es valido"),
	USUARIO_INVALIDO(1, "Usuario invalido");
	
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
	
	public JSONObject toJson() throws JSONException {
			return new JSONObject(this.toString());
	}
}