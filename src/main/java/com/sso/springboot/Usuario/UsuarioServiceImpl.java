package com.sso.springboot.Usuario;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	UsuarioDAO usuarioDAO;
	
	@Transactional(readOnly = true)
	public Optional<Usuario> findById(Long id) {
		return usuarioDAO.findById(id);
	}

	@Override
	public Usuario save(Usuario usuario) {
		return usuarioDAO.save(usuario);
	}
	
	@Override
	public Usuario update(Usuario usuario) {
		return usuarioDAO.save(usuario);
	}
	
	@Transactional(readOnly = true)
	public Optional<Usuario> findByUserAndPassAndApiKey(String user, String pass, String apiKey){
		
		Optional<Usuario> usuario  = usuarioDAO.findByUsuarioAndPassword(user, pass);
			
		if (usuario.isPresent() && usuario.get().getTenant().getApiKey().equals(apiKey) && usuario.get().isEnable()) {
			return usuario;
		}else {
			return null; 
		}
	}
}
