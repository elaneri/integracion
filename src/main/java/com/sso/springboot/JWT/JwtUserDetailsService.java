package com.sso.springboot.JWT;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sso.springboot.Usuario.Usuario;
import com.sso.springboot.Usuario.UsuarioDAO;
import com.sso.springboot.Usuario.UsuarioService;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	UsuarioDAO usuarioDAO;

	
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		
		
		Long id = Long.valueOf(userId);
		
		Optional<Usuario> usuario = usuarioDAO.findById(id);
		
		if (usuario.isPresent() && usuario.get().isEnable()) {
			String usId = String.valueOf(usuario.get().getIdUsuario());
			
			return new User(usId, usuario.get().getPassword(),
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("Usuario con encontrado: " + userId);
		}
	}

}