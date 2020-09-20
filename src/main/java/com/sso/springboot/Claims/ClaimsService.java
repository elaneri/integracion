package com.sso.springboot.Claims;

import java.util.List;


public interface ClaimsService {
	public Claim findByNombre(String nombre);
	public List<Claim> getValidClaims();
}