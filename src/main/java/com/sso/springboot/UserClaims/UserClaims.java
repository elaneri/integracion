package com.sso.springboot.UserClaims;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sso.springboot.Claims.Claim;
import com.sso.springboot.Tenant.Tenant;
import com.sso.springboot.Usuario.Usuario;

@Entity
@Table(name = "userClaims")
public class UserClaims implements Serializable {

	private static final long serialVersionUID = 3083839705410549599L;

	@Id
	@Column(name = "id_user_claims")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUserClaims;

	@Column(nullable = false, length = 200)
	private String claimValue;

	@OneToOne
	@JoinColumn(name = "id_claim", nullable = false)
	private Claim claim;

	@OneToOne
	@JoinColumn(name = "id_usuario", nullable = false)
	private Usuario usuario;

	public void setClaimValue(String claimValue) {
		this.claimValue = claimValue;
	}

	public String getClaimValue() {
		return claimValue;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim c) {
		this.claim = c;
	}
	
	
	public void setUsuario(Usuario u) {
		this.usuario = u;
	}
	
	
}
