package com.sso.springboot.Usuario;

import java.util.Optional;

public interface UsuarioService {
	
	public Usuario save(Usuario usuario);
	
	public Optional<Usuario> findByUserAndPassAndApiKey(String user, String pass, Optional <String> apiKey);
	
	public Optional<Usuario> findByUserAndPass(String user, String pass);
	
	public Usuario update(Usuario usuario);
	
}
