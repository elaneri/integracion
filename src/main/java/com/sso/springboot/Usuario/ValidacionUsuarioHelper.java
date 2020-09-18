package com.sso.springboot.Usuario;

public class ValidacionUsuarioHelper {

	public static void validarUsuario(Usuario usuario) throws Exception{
		
		
	
		
		//TODO:verificar porqué en postman no figura detalle de la excepcion...
		if( usuario.getNombre().trim().equals(""))
			throw new Exception("El nombre de usuario no puede estar vacío");
		
		if( usuario.getPassword().trim().equals(""))
			throw new Exception("La contraseña del usuario no puede estar vacía");
		
		if( usuario.getPassword().trim().length() < 8)
			throw new Exception("La contraseña del usuario debe contener al menos 8 caracteres");

		
		
	}
}
