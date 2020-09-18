package com.sso.springboot.UserClaims;

import java.io.Serializable;

public class RequestClaim implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4621299318056286225L;
	private String nombre;
	private String valor;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}

}
