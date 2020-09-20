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

	// GET: http://localhost:1317/Usuarios/1
	@RequestMapping(value = "/{idUsuario}")
	public ResponseEntity<Usuario> getUsuarioByID(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") Long id) throws Exception {

		Optional<Usuario> usuario = usuarioService.findById(id);
		if (usuario.isPresent()) {
			if (!usuario.get().getTenant().getApiKey().equals(apk)) {
				//TODO: ver si se puede ver a excepcion en postman..... 
				//validacion en caso de que el usuario pertenezca a otro tenant del que quiere visualizar....
				throw new Exception("No se puede visualizar el usuario. Permiso denegado!");
			}
			usuario.get().setPassword("");
			return ResponseEntity.ok(usuario.get());
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	// POST: http://localhost:8080/Usuarios
	@PostMapping
	public ResponseEntity<Usuario> crearUsuario(@RequestHeader("x-api-key") String apk, @RequestBody Usuario usuario)
			throws Exception {

		//TODO:Codear unicidad por nombreusuario + apikey
		UsuarioHelper.validarUsuario(usuario);
		Usuario nuevoUsuario = usuarioService.save(usuario,apk);
		return ResponseEntity.ok(nuevoUsuario);
	}

	// PUT: http://localhost:8080/Usuarios/1
	//Antes buscaba por idUsuario, ahora busca por nombreUsuario
	//TODO: actualizar wiki... 
	@RequestMapping(value = "/{idUsuario}", method = RequestMethod.PUT)
	public ResponseEntity<Usuario> actualizarUsuario(@RequestHeader("x-api-key") String apk,
			@PathVariable("nombreUsuario") String nombreUsuario, @RequestBody Usuario usuarioModificado) throws Exception {

		Optional<Usuario> usuarioExistente = usuarioService.findByUserName(nombreUsuario);

		if (usuarioExistente.isPresent()) {
			if (!usuarioExistente.get().getTenant().getApiKey().equals(apk)) {
				//TODO: ver si se puede ver a excepcion en postman..... 
				//validacion en caso de que el usuario pertenezca a otro tenant del que quiere actualizar....
				throw new Exception("No se puede modificar el usuario. Permiso denegado!");
			}
			UsuarioHelper.validarUsuario(usuarioModificado);
			
			return ResponseEntity.ok(usuarioService.update(usuarioExistente.get(), usuarioModificado, apk));
		} else {
			return ResponseEntity.noContent().build();
		}
	}
	
	// PUT: http://localhost:8080/Usuarios/Eliminar/1
	@RequestMapping(value = "/Eliminar/{idUsuario}", method = RequestMethod.PUT)
	public ResponseEntity<Usuario> eliminarUsuario(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") long idUsuario) throws Exception {

		Optional<Usuario> usuario = usuarioService.findById(idUsuario);

		if (usuario.isPresent()) {
			if (!usuario.get().getTenant().getApiKey().equals(apk)) {
				//TODO: ver si se puede ver a excepcion en postman..... 
				//validacion en caso de que el usuario pertenezca a otro tenant del que quiere eliminar....
				throw new Exception("No se puede eliminar el usuario. Permiso denegado!");
			}
			usuario.get().setEnable(false);
			return ResponseEntity.ok(usuarioService.save(usuario.get(), apk));
		} else {
			return ResponseEntity.noContent().build();
		}
	}
}
