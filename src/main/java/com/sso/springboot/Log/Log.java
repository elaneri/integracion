package com.sso.springboot.Log;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sso.springboot.Tenant.Tenant;
import com.sso.springboot.TipoLog.TipoLog;
import com.sso.springboot.Usuario.Usuario;

@Entity
@Table(name="logs")
public class Log implements Serializable {
	
	private static final long serialVersionUID = -3395237410645768606L;

	@Id
	@Column(name="id_log")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idLog;
	
	@ManyToOne
	@JoinColumn(name="id_usuario",nullable=false)
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name="id_tenant",nullable=false)
	private Tenant tenant;
	
	@ManyToOne
	@JoinColumn(name="id_tipo_log",nullable=false)
	private TipoLog tipo;
	
	
	@Column(nullable=false,length=8)
	private String fecha;
	
	@Column(nullable=false,length=100)
	private String log;
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public TipoLog getTipo() {
		return tipo;
	}

	public void setTipo(TipoLog tipo) {
		this.tipo = tipo;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

}
