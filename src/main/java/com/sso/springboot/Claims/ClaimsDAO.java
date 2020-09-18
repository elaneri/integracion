package com.sso.springboot.Claims;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sso.springboot.UserClaims.UserClaims;
import com.sso.springboot.Usuario.Usuario;


public interface ClaimsDAO extends JpaRepository<Claim, Long> {

	@Query("select c from Claim c where c.nombre =?1")
	Claim findByNombre(String nombre);

}