package com.sso.springboot.Claims;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sso.springboot.Tenant.Tenant;

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
}
