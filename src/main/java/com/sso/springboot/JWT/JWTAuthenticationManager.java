package com.sso.springboot.JWT;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.sso.springboot.Messages.SSOMessages;
import com.sso.springboot.Usuario.Usuario;
import com.sso.springboot.Usuario.UsuarioService;

@Service

public class JWTAuthenticationManager implements AuthenticationManager{

	@Autowired
	private UsuarioService userService;

    private static final Logger LOG = LoggerFactory.getLogger(JWTAuthenticationManager.class);

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		String apk = (String)authentication.getDetails();
		
		if(apk == null ) {
			LOG.warn(SSOMessages.API_KEY_INCORRECTA.toString() + apk);
			throw new BadCredentialsException(SSOMessages.API_KEY_INCORRECTA.toString());
		}
		
		Optional<Usuario> usuario = userService.findByUserAndPassAndApiKey
							(authentication.getPrincipal().toString(),
									authentication.getCredentials().toString(),apk);
		
		
		if(usuario == null || !usuario.isPresent() || !usuario.get().isEnable())  {
			LOG.warn(SSOMessages.USUARIO_PASSWORD_INCORRECTO.toString());
					throw new BadCredentialsException(SSOMessages.USUARIO_PASSWORD_INCORRECTO.toString());
		}

		return authentication;
	}
}
