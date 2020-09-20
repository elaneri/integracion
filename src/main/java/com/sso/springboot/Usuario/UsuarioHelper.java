package com.sso.springboot.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class UsuarioHelper {

	public static void validarUsuario(Usuario usuario) throws Exception{
		
		//Validaciones a realizar
		String rg_digitos = ".*\\d+.*";
		String rg_onelowercase = ".*[a-z].*";
		String rg_oneuppercase = ".*[A-Z].*";
		String rg_specialchar = ".*[.@#$%!.].*";
		String rg_length = ".{5,40}";
		String rg_email = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		String rg_fecha = "\\d{6}";
		String rg_boolean = "[true|false]";
		
		//Agregue estas 2 validaciones porque las borraron
		//Valida que el usuario no esté vacio y supere 10 caracteres
		//Validaciones usuario
		if(usuario.getNombre().trim().equals(""))
			throw new Exception("El nombre de usuario no puede estar vacío");
		
		if(usuario.getNombre().trim().length() > 10)
			throw new Exception("El nombre de usuario no puede contener más de 10 caracteres");
		
		
		//Validaciones password		
		if (!Pattern.matches(rg_digitos, usuario.getPassword())) {
			throw new IllegalArgumentException ("El password debe tener al menos un número");
		}
		
		if (!Pattern.matches(rg_onelowercase, usuario.getPassword())) {
			throw new Exception("El password debe tener al menos una minúscula");
		}
		
		if (!Pattern.matches(rg_oneuppercase, usuario.getPassword())) {
			throw new Exception("El password debe tener al menos una mayúscula");
		}
		
		if (!Pattern.matches(rg_specialchar, usuario.getPassword())) {
			throw new Exception("El password debe tener al menos un caracter especial (@#$%!.)");
		}
		
		if (!Pattern.matches(rg_length, usuario.getPassword())) {
			throw new Exception("El password debe tener al menos 5 caracteres y como máximo 40");
		}				
		
		if( usuario.getPassword().equals(""))
			throw new Exception("La contraseña del usuario no puede estar vacía");
		
		if( usuario.getPassword().trim().length() < 8)
			throw new Exception("La contraseña del usuario debe contener al menos 8 caracteres");	
		
		//Validaciones email
		
		if( !Pattern.matches(rg_email, usuario.getMail()))
			throw new Exception("El email tiene un formato inválido");
		
		//TODO: testear sin funciona.....
		//Validaciones enable
		if( !Pattern.matches(rg_boolean, String.valueOf(usuario.isEnable())))
			throw new Exception("El valor del campo enable es inválido");
		
		
		//Validaciones fecha
		if( !Pattern.matches(rg_fecha, usuario.getFecha_nacimiento()))
			throw new Exception("La fecha de nacimiento tiene un formato inválido");
		
		if( Pattern.matches(rg_fecha, usuario.getFecha_nacimiento())) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			try {
				date = format.parse(usuario.getFecha_nacimiento());
			} catch (ParseException e) {
				throw new Exception("La fecha de nacimiento es inválida");
			}
		}
			

		if( !Pattern.matches(rg_fecha, usuario.getFechaAlta()))
			throw new Exception("La fecha de nacimiento tiene un formato inválido");
		
		if( Pattern.matches(rg_fecha, usuario.getFechaAlta())) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			try {
				date = format.parse(usuario.getFechaAlta());
			} catch (ParseException e) {
				throw new Exception("La fecha de alta es inválida");
			}
		}
		
		if( !usuario.getFechaBaja().equals("")) {
			if(!Pattern.matches(rg_fecha, usuario.getFechaAlta())) 
				throw new Exception("La fecha de baja tiene un formato inválido");				
		}
			
			
		if( Pattern.matches(rg_fecha, usuario.getFechaBaja())) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			try {
				date = format.parse(usuario.getFechaBaja());
			} catch (ParseException e) {
				throw new Exception("La fecha de baja es inválida");
			}

		}
	}
	
	public static Usuario modificar(Usuario usuarioExistente, Usuario usuarioNuevo) throws Exception{
		
		//TODO: no se toma en cuenta el usuario, el campo enabled, la fecha de alta, la fecha de baja.......
		usuarioExistente.setNombre(usuarioNuevo.getNombre().trim());
		usuarioExistente.setApellido(usuarioNuevo.getApellido().trim());
		usuarioExistente.setPassword(usuarioNuevo.getPassword().trim());
		usuarioExistente.setMail(usuarioNuevo.getMail().trim());
		usuarioExistente.setFecha_nacimiento(usuarioNuevo.getFecha_nacimiento().trim());
		usuarioExistente.setTelefono(usuarioNuevo.getTelefono().trim());
		usuarioExistente.setPropiedades(usuarioNuevo.getPropiedades());
		
		return usuarioExistente;
	}
}
		