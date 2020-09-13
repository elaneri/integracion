package com.sso.springboot.JWT;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.sso.springboot.Usuario.Usuario;
import com.sso.springboot.Usuario.UsuarioService;

@Service

public class JWTAuthenticationManager implements AuthenticationManager{

	@Autowired
	private UsuarioService userService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		String apk = (String)authentication.getDetails();
		
		if(apk == null ) {
			throw new BadCredentialsException("ApiKey incorrecta.");
		}
		
		Optional<Usuario> usuario = userService.findByUserAndPassAndApiKey
							(authentication.getPrincipal().toString(),
									authentication.getCredentials().toString(),apk);
		
		
		if(usuario == null || !usuario.isPresent() || !usuario.get().isEnable())  {
			throw new BadCredentialsException("Usuario o Password incorrectos.");
		}

		return authentication;
	}
}
