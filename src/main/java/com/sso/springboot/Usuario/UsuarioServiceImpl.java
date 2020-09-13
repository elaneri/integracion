package com.sso.springboot.Usuario;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(usuario.getPassword().trim());
		usuario.setPassword(encodedPassword);
		return usuarioDAO.save(usuario);
	}
	
	@Override
	public Usuario update(Usuario usuario) {
		return usuarioDAO.save(usuario);
	}
	
	@Transactional(readOnly = true)
	public Optional<Usuario> findByUserAndPassAndApiKey(String user, String pass, String apiKey){
		
		Optional<Usuario> usuario  = usuarioDAO.findByUsuario(user);
				
		if (usuario.isPresent() && new BCryptPasswordEncoder().matches(pass, usuario.get().getPassword().trim()) 
												&& usuario.get().getTenant().getApiKey().equals(apiKey) && usuario.get().isEnable()) {
			return usuario;
		}else {
			return null; 
		}
	}
	
	@Override
	public Optional<Usuario> findByUserName(String user) {
		
		Optional<Usuario> usuario  = usuarioDAO.findByUsuario(user);
		
		if (usuario != null && usuario.isPresent() && usuario.get().isEnable()) {
			return usuario;
		}else {
			return null; 
		}
	}
}
