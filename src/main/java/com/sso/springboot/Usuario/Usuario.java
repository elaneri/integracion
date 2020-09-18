package com.sso.springboot.Usuario;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.databind.JsonNode;
import com.sso.springboot.Claims.UserClaims;
import com.sso.springboot.Tenant.Tenant;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

@Entity
@Table(name = "usuarios")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Usuario implements Serializable {

	private static final long serialVersionUID = -3359031128100969764L;

	@Id
	@Column(name = "id_usuario")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUsuario;

	@ManyToOne
	@JoinColumn(name = "id_tenant", nullable = false)
	private Tenant tenant;

	@Column(nullable = false, length = 15)
	private String nombre;

	@Column(nullable = false, length = 15)
	private String apellido;

	@Column(nullable = false, length = 20, unique = true)
	private String usuario;

	@Column(nullable = false, length = 255)
	private String password;

	@Column(nullable = false, length = 30)
	private String mail;

	@Column(nullable = false, length = 8)
	@NotNull(message = "Fecha de nacimiento: Campo requerido")
	private String fecha_nacimiento;

	@Column(nullable = false, length = 20)
	private String telefono;

	@Column(nullable = false)
	private boolean enable;

	@Column(nullable = false, length = 8)
	private String fechaAlta;

	@Column(nullable = true, length = 8)
	private String fechaBaja;

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private JsonNode propiedades;

	@OneToMany
	@JoinColumn(name = "id_user_claims", nullable = true)
	private List<UserClaims> userClaims;

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getFecha_nacimiento() {
		return fecha_nacimiento;
	}

	public void setFecha_nacimiento(String fecha_nacimiento) {
		this.fecha_nacimiento = fecha_nacimiento;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
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

	public JsonNode getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(JsonNode propiedades) {
		this.propiedades = propiedades;
	}

	public void setUserClaims(List<UserClaims> usrCl) {
		this.userClaims = usrCl;
	}

	public Map<String, Object> getClaims() {

		Map<String, Object> claims = new HashMap<>();

		claims.put("iss", "sso");

		for (UserClaims c : this.userClaims) {

			claims.put(c.getClaim().getNombre(), c.getClaimValue());

		}

		return claims;
	}
}