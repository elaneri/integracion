package com.sso.springboot.UserClaims;

import java.util.List;
import java.util.Optional;

import com.sso.springboot.Claims.Claim;
import com.sso.springboot.Usuario.Usuario;



public interface UserClaimsService {
	public List<UserClaims> findClaimsForUser(Usuario usuario);
	
	public Optional<UserClaims> findClaimForUser(Usuario usuario, Claim claim);
	
	public UserClaims save(UserClaims userClaim);


}
