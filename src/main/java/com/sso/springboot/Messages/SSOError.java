package com.sso.springboot.Messages;

public enum SSOError {
	
	TENANT_API_KEY(0, "Error Tenant ApiKey no valida"),
	JWT_ERROR(1,"No se ha podido obtener el Token JWT"), 
	JWT_EXPIRADO(2, "El Token JWT ha expirado"), 
	CLAIM_DUPLICADA(3, "El usuario ya posee un claim con el mismo nombre"), 
	CLAIM_NO_VALIDA(4, "El nombre de claim no es valido"),
	USUARIO_INVALIDO(5, "Usuario invalido"),
	ENCODING_INVALIDO(6, "Encoding invalido"),
	JWT_BARRER_MISSING(7,"El Token JWT no comienza con la palabra Bearer"),
	JWT_INVALIDO(8,"El Token JWT no es valido"),
	VER_USUARIO_DENEGADO(9,"Error al visualizar el usuario. Permiso denegado!"),
	USUARIO_NO_ENCONTRADO(10,"Usuario no encontrado"),
	USUARIO_EXISTENTE(11,"Usuario existente"),
	MODIFICAR_USUARIO_DENEGADO(11,"No se puede modificar el usuario. Permiso denegado!"),
	ELIMINAR_USUARIO_DENEGADO(11,"No se puede eliminar el usuario. Permiso denegado!"),
	HABILITAR_USUARIO_DENEGADO(11,"No se puede habilitar el usuario. Permiso denegado!");
	
	private final int code;
	private final String description;

	private SSOError(int code, String description) {
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