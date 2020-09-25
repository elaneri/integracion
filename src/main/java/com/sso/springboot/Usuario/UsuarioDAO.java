package com.sso.springboot.Usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sso.springboot.Tenant.Tenant;

public interface UsuarioDAO extends JpaRepository<Usuario, Long>  {

	Optional<Usuario> findByUsuarioAndTenant(String user, Tenant ten);
}