package com.sso.springboot.Tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tenants")
public class Tenant implements Serializable {
	
	private static final long serialVersionUID = -3395237410645768606L;

	@Id
	@Column(name="id_tenant")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false,length=50)
	private String nombre;
	
	@Column(nullable=false,length=100)
	private String descripcion;
	
	@Column(nullable=false,length=30)
	private String apiKey;
	
	@Column(nullable=true,length=50)
	private String callbackSuccess;
	
	@Column(nullable=true,length=50)
	private String callbackError;
	
	@Column(nullable=false,length=8)
	private String fechaAlta;
	
	@Column(nullable=true,length=8)
	private String fechaBaja;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getCallbackSuccess() {
		return callbackSuccess;
	}

	public void setCallbackSuccess(String callbackSuccess) {
		this.callbackSuccess = callbackSuccess;
	}

	public String getCallbackError() {
		return callbackError;
	}

	public void setCallbackError(String callbackError) {
		this.callbackError = callbackError;
	}

	public String getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public String getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(String fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	
}
