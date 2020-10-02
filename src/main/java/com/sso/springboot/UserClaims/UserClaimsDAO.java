package com.sso.springboot.UserClaims;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sso.springboot.Claims.Claim;
import com.sso.springboot.Usuario.Usuario;


public interface UserClaimsDAO extends JpaRepository<UserClaims, Long> {
	
	@Query("select uc from UserClaims uc where uc.usuario =?1")
	List<UserClaims> findClaimsForUser(Usuario usuario);
	
	@Query("select uc from UserClaims uc where uc.usuario =?1 and uc.claim=?2")
	Optional<UserClaims>  findClaimForUser(Usuario usuario, Claim claim);
	
	UserClaims save(UserClaims userClaim);
	
	void delete(UserClaims userClaim);

}
