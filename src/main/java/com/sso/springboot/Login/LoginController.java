package com.sso.springboot.Login;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sso.springboot.JWT.JwtRequest;
import com.sso.springboot.JWT.JwtResponse;
import com.sso.springboot.JWT.JwtTokenUtil;
import com.sso.springboot.Usuario.Usuario;
import com.sso.springboot.Usuario.UsuarioService;


@RestController
@RequestMapping("/Login")
public class LoginController {
	@Autowired
	private UsuarioService userService;
    
    @Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
    @PostMapping
	public ResponseEntity<?> loginUsuario(@RequestBody JwtRequest authenticationRequest) throws Exception{
    	
    	try {
    		UsernamePasswordAuthenticationToken authToken = 	new UsernamePasswordAuthenticationToken
			(authenticationRequest.getUsername(), 
					authenticationRequest.getPassword());
    		
    		authToken.setDetails(authenticationRequest.getApiKey());
    		
    		authenticationManager.authenticate(authToken);
    		
    		/* esta parte se ejecuta si authenticationManager esta ok*/
    		Optional<Usuario> usuario = userService.findByUserAndPassAndApiKey
					(authenticationRequest.getUsername(),
							authenticationRequest.getPassword(),authenticationRequest.getApiKey());

    		
    		Map<String, Object> claims = new HashMap<>();
    		claims.put("userID",usuario.get().getIdUsuario());
			final String token = "Bearer " + jwtTokenUtil.generateToken(authenticationRequest.getUsername(), claims);

    		return ResponseEntity.ok(new JwtResponse(token));


		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
    }
	
}