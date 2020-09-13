package com.sso.springboot.JWT;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sso.springboot.Usuario.Usuario;
import com.sso.springboot.Usuario.UsuarioService;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UsuarioService ususarioService;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Usuario usuario = ususarioService.findByUserName(username).get();
		
		if (usuario != null && usuario.isEnable()) {
			return new User(usuario.getUsuario(), usuario.getPassword(),
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("Usuario con encontrado: " + username);
		}
	}

}