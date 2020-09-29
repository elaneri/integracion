package com.sso.springboot.JWT;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sso.springboot.Messages.SSOError;
import com.sso.springboot.Usuario.Usuario;
import com.sso.springboot.Usuario.UsuarioDAO;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	UsuarioDAO usuarioDAO;

	private static final Logger LOG = LoggerFactory.getLogger(JwtUserDetailsService.class);

	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		
		
		Long id = Long.valueOf(userId);
		
		Optional<Usuario> usuario = usuarioDAO.findById(id);
		
		if (usuario.isPresent() && usuario.get().isEnable()) {
			String usId = String.valueOf(usuario.get().getIdUsuario());
			
			return new User(usId, usuario.get().getPassword(),
					new ArrayList<>());
		} else {
			LOG.warn(SSOError.USUARIO_NO_ENCONTRADO + ": " + userId);
			throw new UsernameNotFoundException(SSOError.USUARIO_NO_ENCONTRADO + ": " + userId);
		}
	}
}