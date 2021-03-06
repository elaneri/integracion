package com.sso.springboot.Usuario;

import java.util.Optional;

public interface UsuarioService {
	
	public Usuario save(Usuario usuario, String apk);
	
	public Optional<Usuario> findByUserAndPassAndApiKey(String user, String pass, String apiKey);
	
	public Usuario update(Usuario usuarioExistente, Usuario usuarioModificado);
	
	public Usuario updatePassword(Usuario usuarioExistente, Usuario usuarioNuevoPass, String apk);
	
	public Usuario delete(Usuario usuario, String apk);
	
	public Usuario activate(Usuario usuario, String apk);
	
	public Optional<Usuario> findByUserNameAndTenant(String user, String apiKey);
	
	public Optional<Usuario> findByUserId(String userId);

	public Optional<Usuario> findByUserIdAndTenant(String userId, String apiKey);
	


}
