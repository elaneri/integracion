package com.sso.springboot.Claims;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "claim")
public class Claim implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9063894241996969253L;

	@Id
	@Column(name = "id_claim")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idClaim;

	@Column(nullable = false, length = 100)
	private String nombre;

	@Column(nullable = false, length = 200)
	private String descripcion;

	public String getNombre() {

		return nombre;
	}

	public String getDescripcion() {

		return descripcion;
	}

	public void setNombre(String nombre) {

		this.nombre = nombre;
	}
	
	public Long getIdClaim() {

		return idClaim;
	}

}
