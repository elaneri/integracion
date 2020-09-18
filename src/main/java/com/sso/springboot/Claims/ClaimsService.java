package com.sso.springboot.Claims;

import java.util.List;

import com.sso.springboot.UserClaims.UserClaims;
import com.sso.springboot.Usuario.Usuario;

public interface ClaimsService {
	public Claim findByNombre(String nombre);
}