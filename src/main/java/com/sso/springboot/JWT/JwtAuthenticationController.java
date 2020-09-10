package com.sso.springboot.JWT;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sso.springboot.Usuario.UsuarioServiceImpl;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
	@Autowired
	private UsuarioServiceImpl usuarioService;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	@RequestMapping(value = "/LoginOld", method = RequestMethod.POST)
	public ResponseEntity<?> generateAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
			throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword(),authenticationRequest.getApiKey());

		/*final UserDetails userDetails = jwtInMemoryUserDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());
*/
		
		//userDetails
		final String token = jwtTokenUtil.generateToken(authenticationRequest.getUsername());

		return ResponseEntity.ok(new JwtResponse(token));
	}

	private void authenticate(Object username, Object password, String apikey) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		Objects.requireNonNull(apikey);

		try {

			//Optional<Usuario> usuario = null;
			//usuario = usuarioService.findByUserAndPassAndApiKey(username, password, Optional.of(apikey));

			//if (usuario.isPresent()) {

				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			//}

		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}