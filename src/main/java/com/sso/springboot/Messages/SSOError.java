package com.sso.springboot.Messages;

public enum SSOError {
	
	TENANT_API_KEY(0, "Error Tenant ApiKey no valida"),
	API_KEY_INCORRECTA(1, "ApiKey incorrecta"),
	JWT_ERROR_OBTENER_JWT(2,"No se ha podido obtener el Token JWT"), 
	JWT_EXPIRADO(3, "El Token JWT ha expirado"), 
	JWT_ACTUALIZADO(4, "El Token puede ser actualizado"), 
	CLAIM_DUPLICADA(5, "El usuario ya posee un claim con el mismo nombre"), 
	CLAIM_NO_VALIDA(6, "El nombre de claim no es valido"),
	USUARIO_INVALIDO(7, "Usuario invalido"),
	ENCODING_INVALIDO(8, "Encoding invalido"),
	JWT_BARRER_MISSING(9,"El Token JWT no comienza con la palabra Bearer"),
	JWT_INVALIDO(10,"El Token JWT no es valido"),
	VER_USUARIO_DENEGADO(11,"Error al visualizar el usuario. Permiso denegado!"),
	USUARIO_NO_ENCONTRADO(12,"Usuario no encontrado"),
	USUARIO_EXISTENTE(13,"Usuario existente"),
	MODIFICAR_USUARIO_DENEGADO(14,"No se puede modificar el usuario. Permiso denegado!"),
	ELIMINAR_USUARIO_DENEGADO(15,"No se puede eliminar el usuario. Permiso denegado!"),
	HABILITAR_USUARIO_DENEGADO(16,"No se puede habilitar el usuario. Permiso denegado!"),
	USUARIO_DESHABILITADO(17,"Usuario deshabilitado"),
	USUARIO_PASSWORD_INCORRECTO(18,"Usuario o Password incorrectos"),
	CREDENCIALES_INVALIDAS(19,"Credenciales inválidas");
	
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