package com.sso.springboot.Usuario;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.sso.springboot.Messages.SSOError;
import com.sso.springboot.Validaciones.UsuarioHelper;


@RestController
@RequestMapping("/Usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioServiceImpl usuarioService;
	
	private static final Logger LOG = LoggerFactory.getLogger(UsuarioController.class);

	// GET: http://localhost:1317/Usuarios/1
	@RequestMapping(value = "/{idUsuario}")
	public ResponseEntity<Usuario> getUsuarioByID(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") Long id) throws Exception {

		Optional<Usuario> usuario = usuarioService.findById(id);
		if (usuario.isPresent()) {
			if (!usuario.get().getTenant().getApiKey().equals(apk.trim())) {
				//validacion en caso de que el usuario pertenezca a otro tenant del que quiere visualizar....
				LOG.warn(SSOError.VER_USUARIO_DENEGADO.toString());
				throw new ResponseStatusException(
						HttpStatus.FORBIDDEN, SSOError.VER_USUARIO_DENEGADO.toString());
			}
			usuario.get().setPassword("???");
			return ResponseEntity.ok(usuario.get());
		} else {
			LOG.warn(SSOError.USUARIO_NO_ENCONTRADO.toString());
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, SSOError.USUARIO_NO_ENCONTRADO.toString());
		}
	}

	// POST: http://localhost:8080/Usuarios
	@PostMapping
	public ResponseEntity<Usuario> crearUsuario(@RequestHeader("x-api-key") String apk, @RequestBody Usuario usuario)
			throws Exception {

		
		Optional<Usuario> usuarioExistente = usuarioService.findByUserNameAndTenant(usuario.getUsuario().trim(), apk);
		
		if (usuarioExistente != null && usuarioExistente.isPresent() && usuarioExistente.get().getTenant().getApiKey().equals(apk.trim())) {
			LOG.warn(SSOError.USUARIO_EXISTENTE.toString());
			throw new ResponseStatusException(
					HttpStatus.CONFLICT, SSOError.USUARIO_EXISTENTE.toString());
			
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
				LOG.warn(SSOError.MODIFICAR_USUARIO_DENEGADO.toString());
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, SSOError.MODIFICAR_USUARIO_DENEGADO.toString());
			}
			
			UsuarioHelper.validarUsuario(usuarioModificado, AccionUsuario.MODIFICACION);
			Usuario usuario = usuarioService.update(usuarioExistente.get(), usuarioModificado, apk);
			usuario.setPassword("???");
			
			return ResponseEntity.ok(usuario);
		} else {
			
			LOG.warn(SSOError.USUARIO_NO_ENCONTRADO.toString());
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, SSOError.USUARIO_NO_ENCONTRADO.toString());
		}
	}
	
	// PUT: http://localhost:8080/Usuarios/Eliminar/1
	@RequestMapping(value = "/Eliminar/{idUsuario}", method = RequestMethod.PUT)
	public ResponseEntity<Usuario> eliminarUsuario(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") long idUsuario) throws Exception {

		Optional<Usuario> usuario = usuarioService.findById(idUsuario);

		if (usuario != null && usuario.isPresent()) {
			if (!usuario.get().getTenant().getApiKey().equals(apk.trim())) {
				LOG.warn(SSOError.ELIMINAR_USUARIO_DENEGADO.toString());
				//validacion en caso de que el usuario pertenezca a otro tenant del que quiere eliminar....
				throw new ResponseStatusException(
						HttpStatus.FORBIDDEN, SSOError.ELIMINAR_USUARIO_DENEGADO.toString());
			}
			
			Usuario usuarioEliminado = usuarioService.delete(usuario.get(), apk);
			usuarioEliminado.setPassword("???");
			
			return ResponseEntity.ok(usuarioEliminado);
		} else {
			
			LOG.warn(SSOError.USUARIO_NO_ENCONTRADO.toString());
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, SSOError.USUARIO_NO_ENCONTRADO.toString());
		}
	}
	
	// PUT: http://localhost:8080/Usuarios/Habilitar/1
	@RequestMapping(value = "/Habilitar/{idUsuario}", method = RequestMethod.PUT)
	public ResponseEntity<Usuario> habilitarUsuario(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") long idUsuario) throws Exception {

		Optional<Usuario> usuario = usuarioService.findById(idUsuario);

		if (usuario != null && usuario.isPresent()) {
			if (!usuario.get().getTenant().getApiKey().equals(apk.trim())) {
				LOG.warn(SSOError.HABILITAR_USUARIO_DENEGADO.toString());
				//validacion en caso de que el usuario pertenezca a otro tenant del que quiere habilitar....
				throw new ResponseStatusException(
						HttpStatus.FORBIDDEN, SSOError.HABILITAR_USUARIO_DENEGADO.toString());
			}
				
			Usuario usuarioHabilitado = usuarioService.activate(usuario.get(), apk);
			usuarioHabilitado.setPassword("???");
				
			return ResponseEntity.ok(usuarioHabilitado);
		} else {
			LOG.warn(SSOError.USUARIO_NO_ENCONTRADO.toString());
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, SSOError.USUARIO_NO_ENCONTRADO.toString());
		}
	}
}
