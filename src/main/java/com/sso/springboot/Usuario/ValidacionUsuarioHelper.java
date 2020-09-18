package com.sso.springboot.Usuario;

import com.sso.springboot.Messages.JWTError;

public class ValidacionUsuarioHelper {

	public static void validarUsuario(Usuario usuario) throws Exception{
		
		
	
		
		//TODO:verificar porqué en postman no figura detalle de la excepcion...
		if( usuario.getNombre().trim().equals(""))
			throw new Exception(JWTError.USUARIO_NOMBRE.toString());
		
		if( usuario.getPassword().trim().equals(""))
			throw new Exception("La contraseña del usuario no puede estar vacía");
		
		if( usuario.getPassword().trim().length() < 8)
			throw new Exception("La contraseña del usuario debe contener al menos 8 caracteres");

		
		
	}
}
