package com.sso.springboot.Usuario;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
			if (!usuario.get().getTenant().getApiKey().equals(apk.trim())) {
				//validacion en caso de que el usuario pertenezca a otro tenant del que quiere visualizar....
				throw new ResponseStatusException(
						HttpStatus.FORBIDDEN, "Error al visualizar el usuario. Permiso denegado!");
			}
			usuario.get().setPassword("???");
			return ResponseEntity.ok(usuario.get());
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Usuario no encontrado");
		}
	}

	// POST: http://localhost:8080/Usuarios
	@PostMapping
	public ResponseEntity<Usuario> crearUsuario(@RequestHeader("x-api-key") String apk, @RequestBody Usuario usuario)
			throws Exception {

		Optional<Usuario> usuarioExistente = usuarioService.findByUserNameAndTenant(usuario.getUsuario().trim(), apk);
		
		if (usuarioExistente != null && usuarioExistente.isPresent() && usuarioExistente.get().getTenant().getApiKey().equals(apk.trim())) {
			throw new ResponseStatusException(
					HttpStatus.CONFLICT, "Usuario existente");
			
		}
		
		Date fechaActual = new Date();
		usuario.setFechaAlta(UsuarioHelper.convertirFechaAFormatoJapones(fechaActual));
		
		UsuarioHelper.validarUsuario(usuario, AccionUsuario.ALTA);
		
		Usuario nuevoUsuario = usuarioService.save(usuario,apk);
		
		nuevoUsuario.setPassword("???");
		
		return ResponseEntity.ok(nuevoUsuario);
	}

	// PUT: http://localhost:8080/Usuarios/1
	@RequestMapping(value = "/{idUsuario}", method = RequestMethod.PUT)
	public ResponseEntity<Usuario> actualizarUsuario(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") long idUsuario, @RequestBody Usuario usuarioModificado) throws Exception {

		Optional<Usuario> usuarioExistente = usuarioService.findById(idUsuario);

		if (usuarioExistente != null && usuarioExistente.isPresent()) {
			if (!usuarioExistente.get().getTenant().getApiKey().equals(apk.trim())) {
				//validación en caso de que el usuario pertenezca a otro tenant del que quiere actualizar....
				throw new Exception("No se puede modificar el usuario. Permiso denegado!");
			}
			UsuarioHelper.validarUsuario(usuarioModificado, AccionUsuario.MODIFICACION);

			Usuario usuario = usuarioService.update(usuarioExistente.get(), usuarioModificado, apk);
			usuario.setPassword("???");
			
			return ResponseEntity.ok(usuario);
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Usuario no encontrado");
		}
	}
	
	// PUT: http://localhost:8080/Usuarios/Eliminar/1
	@RequestMapping(value = "/Eliminar/{idUsuario}", method = RequestMethod.PUT)
	public ResponseEntity<Usuario> eliminarUsuario(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") long idUsuario) throws Exception {

		Optional<Usuario> usuario = usuarioService.findById(idUsuario);

		if (usuario != null && usuario.isPresent()) {
			if (!usuario.get().getTenant().getApiKey().equals(apk.trim())) {
				//validacion en caso de que el usuario pertenezca a otro tenant del que quiere eliminar....
				throw new ResponseStatusException(
						HttpStatus.FORBIDDEN, "No se puede eliminar el usuario. Permiso denegado!");
			}
			
			Usuario usuarioEliminado = usuarioService.delete(usuario.get(), apk);
			usuarioEliminado.setPassword("???");
			
			return ResponseEntity.ok(usuarioEliminado);
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Usuario no encontrado");
		}
	}
	
	// PUT: http://localhost:8080/Usuarios/Habilitar/1
	@RequestMapping(value = "/Habilitar/{idUsuario}", method = RequestMethod.PUT)
	public ResponseEntity<Usuario> habilitarUsuario(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") long idUsuario) throws Exception {

		Optional<Usuario> usuario = usuarioService.findById(idUsuario);

		if (usuario != null && usuario.isPresent()) {
			if (!usuario.get().getTenant().getApiKey().equals(apk.trim())) {
				//validacion en caso de que el usuario pertenezca a otro tenant del que quiere habilitar....
				throw new ResponseStatusException(
						HttpStatus.FORBIDDEN, "No se puede habilitar el usuario. Permiso denegado!");
			}
				
			Usuario usuarioHabilitado = usuarioService.activate(usuario.get(), apk);
			usuarioHabilitado.setPassword("???");
				
			return ResponseEntity.ok(usuarioHabilitado);
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Usuario no encontrado");
		}
	}
}
