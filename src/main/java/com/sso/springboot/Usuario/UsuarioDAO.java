package com.sso.springboot.Usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioDAO extends JpaRepository<Usuario, Long>  {

	Optional <Usuario> findByUsuario(String user);
}