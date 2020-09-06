package com.sso.springboot.TipoLog;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tipos_logs")
public class TipoLog implements Serializable {
	
	private static final long serialVersionUID = -3395237410645768606L;

	@Id
	@Column(name="id_tipo_log")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idTipoLog;
	
	@Column(nullable=false,length=20)
	private String descripcion;
	
	public Long getIdTipoLog() {
		return idTipoLog;
	}

	public void setIdTipoLog(Long idTipoLog) {
		this.idTipoLog = idTipoLog;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
