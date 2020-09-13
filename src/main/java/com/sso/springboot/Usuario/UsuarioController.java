package com.sso.springboot.Usuario;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Usuarios")
public class UsuarioController {	
	
    @Autowired
	private UsuarioServiceImpl usuarioService;
	
    //GET: http://localhost:1317/Usuarios/1
  	@RequestMapping(value="/{idUsuario}")
 	public ResponseEntity<Usuario> getUsuarioByID(@RequestHeader("Authorization") String token, @PathVariable("idUsuario") Long id){		
 		


  		
/*
  		String name = Jwts.parser().setSigningKey("TEST").parseClaimsJws("HS512").getBody().get("name", String.class);
  		
        Claims claims;
        try {
            claims = Jwts.parser()
            		.setSigningKey("TEST")
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            
            claims = null;
        }
        */
        
  		Optional<Usuario> usuario = usuarioService.findById(id);
 		if(usuario.isPresent()) {
 			usuario.get().setPassword("");
 			return ResponseEntity.ok(usuario.get());
 		}
 		else {
 			return ResponseEntity.noContent().build();
 		}	
 	}
  	
 	// POST: http://localhost:1317/Usuarios
	@PostMapping
	public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario){
	
		//TODO:codear validaciones
		if (ValidacionUsuarioHelper.esUsuarioValido(usuario)) {
			Usuario nuevoUsuario = usuarioService.save(usuario);
			return ResponseEntity.ok(nuevoUsuario);
		}else {
			return ResponseEntity.noContent().build();
		}
	}
	
	//PUT: http://localhost:1317/Usuarios/Eliminar/1
 	@RequestMapping(value = "/Eliminar/{idUsuario}", method = RequestMethod.PUT)
    public ResponseEntity<Usuario> eliminarUsuario(@PathVariable("idUsuario") long idUsuario) {
 		
 		 Optional<Usuario> usuario = usuarioService.findById(idUsuario);
 		 
 		 if(usuario.isPresent()) {
 			usuario.get().setEnable(false);
 			return ResponseEntity.ok(usuarioService.save(usuario.get()));
 		 }
 		 else {
			return ResponseEntity.noContent().build();
 		 }	
    }
 	 
     //PUT: http://localhost:1317/Usuarios/1
  	 @RequestMapping(value = "/{idUsuario}", method = RequestMethod.PUT)
     public ResponseEntity<Usuario> actualizarUsuario(@PathVariable("idUsuario") long idUsuario,
    		 													@RequestBody Usuario nuevoUsuario) {
  		
  		 Optional<Usuario> usuario = usuarioService.findById(idUsuario);
  		 
  		 if(usuario.isPresent()) {
  			usuario.get().setMail(nuevoUsuario.getMail().trim());
  			usuario.get().setTelefono(nuevoUsuario.getTelefono().trim());
  			return ResponseEntity.ok(usuarioService.save(usuario.get()));
  		 }
  		 else {
 			return ResponseEntity.noContent().build();
  		 }	
     }
}
