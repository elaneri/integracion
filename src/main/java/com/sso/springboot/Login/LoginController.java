package com.sso.springboot.Login;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sso.springboot.Usuario.Usuario;
import com.sso.springboot.Usuario.UsuarioServiceImpl;


@RestController
@RequestMapping("/Login2")
public class LoginController {
	
    @Autowired
	private UsuarioServiceImpl usuarioService;
    
    // POST: http://localhost:1317/Login/user/pass/apiKey
    @PostMapping
	public ResponseEntity<Usuario> loginUsuario(@RequestBody String user,@RequestBody String pass,
																			@RequestBody String apiKey) throws Exception{
    	
    	try {
    		Optional<Usuario> usuario = null;
     		usuario = usuarioService.findByUserAndPassAndApiKey(user, pass, apiKey);
     		
        	if(usuario.isPresent()) {
     			return ResponseEntity.ok(usuario.get());
     		}else {
     			return ResponseEntity.noContent().build();
			}
		} catch (Exception e) {
			throw new Exception("Error en el inicio de sesión");
		}
	}
 	

	
}