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

import com.sso.springboot.Messages.SSOMessages;
import com.sso.springboot.Validaciones.UsuarioHelper;

@RestController
@RequestMapping("/Usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioServiceImpl usuarioService;

	private static final Logger LOG = LoggerFactory.getLogger(UsuarioController.class);

	// GET: http://localhost:1317/Usuarios/1
	@RequestMapping(value = "/{idUsuario}", method = RequestMethod.GET)
	public ResponseEntity<Usuario> getUsuarioByID(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") Long id) throws ResponseStatusException {

		Optional<Usuario> usuario = usuarioService.findById(id);
		try {
			if (usuario.isPresent()) {
				if (!usuario.get().getTenant().getApiKey().equals(apk.trim())) {
					// validacion en caso de que el usuario pertenezca a otro
					// tenant del que quiere visualizar....
					LOG.warn(SSOMessages.VER_USUARIO_DENEGADO.toString());
					throw new ResponseStatusException(HttpStatus.FORBIDDEN,
							SSOMessages.VER_USUARIO_DENEGADO.toString());
				}
				usuario.get().setPassword("???");
				return ResponseEntity.ok(usuario.get());
			} else {
				LOG.warn(SSOMessages.USUARIO_NO_ENCONTRADO.toString());
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, SSOMessages.USUARIO_NO_ENCONTRADO.toString());
			}
		} catch (ResponseStatusException e) {
			LOG.error(e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, SSOMessages.ERROR_GENERICO.toString());
		} catch (Exception e) {
			LOG.error(SSOMessages.ERROR_GENERICO.getDescription(), e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, SSOMessages.ERROR_GENERICO.toString());
		}
	}

	// POST: http://localhost:8080/Usuarios
	@RequestMapping( method = RequestMethod.POST)
	public ResponseEntity<Usuario> crearUsuario(@RequestHeader("x-api-key") String apk, @RequestBody Usuario usuario)
			throws ResponseStatusException {

		Optional<Usuario> usuarioExistente = usuarioService.findByUserNameAndTenant(usuario.getUsuario().trim(), apk);

		if (usuarioExistente != null && usuarioExistente.isPresent()
				&& usuarioExistente.get().getTenant().getApiKey().equals(apk.trim())) {
			LOG.warn(SSOMessages.USUARIO_EXISTENTE.toString());
			throw new ResponseStatusException(HttpStatus.CONFLICT, SSOMessages.USUARIO_EXISTENTE.toString());

		}
		Date fechaActual = new Date();
		usuario.setFechaAlta(UsuarioHelper.convertirFechaAFormatoJapones(fechaActual));

		try {
			UsuarioHelper.validarUsuario(usuario, AccionUsuario.ALTA);
			Usuario nuevoUsuario = usuarioService.save(usuario, apk);
			nuevoUsuario.setPassword("???");
			return ResponseEntity.ok(nuevoUsuario);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
		}
		
		
	}

	// PUT: http://localhost:8080/Usuarios/1
	@RequestMapping(value = "/{idUsuario}", method = RequestMethod.PUT)
	public ResponseEntity<Usuario> actualizarUsuario(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") long idUsuario, @RequestBody Usuario usuarioModificado) throws ResponseStatusException {

		Optional<Usuario> usuarioExistente = usuarioService.findById(idUsuario);

		if (usuarioExistente != null && usuarioExistente.isPresent()) {
			if (!usuarioExistente.get().getTenant().getApiKey().equals(apk.trim())) {
				// validación en caso de que el usuario pertenezca a otro tenant
				// del que quiere actualizar....
				LOG.warn(SSOMessages.MODIFICAR_USUARIO_DENEGADO.toString());
				throw new ResponseStatusException(HttpStatus.FORBIDDEN,
						SSOMessages.MODIFICAR_USUARIO_DENEGADO.toString());
			}

			try{
				
				UsuarioHelper.validarUsuario(usuarioModificado, AccionUsuario.MODIFICACION);
				Usuario usuario = usuarioService.update(usuarioExistente.get(), usuarioModificado, apk);
				usuario.setPassword("???");
				return ResponseEntity.ok(usuario);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
			}
			

		} else {

			LOG.warn(SSOMessages.USUARIO_NO_ENCONTRADO.toString());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, SSOMessages.USUARIO_NO_ENCONTRADO.toString());
		}
	}

	// PUT: http://localhost:8080/Usuarios/Eliminar/1
	@RequestMapping(value = "/Eliminar/{idUsuario}", method = RequestMethod.PUT)
	public ResponseEntity<Usuario> eliminarUsuario(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") long idUsuario) throws ResponseStatusException {

		Optional<Usuario> usuario = usuarioService.findById(idUsuario);

		if (usuario != null && usuario.isPresent()) {
			if (!usuario.get().getTenant().getApiKey().equals(apk.trim())) {
				LOG.warn(SSOMessages.ELIMINAR_USUARIO_DENEGADO.toString());
				// validacion en caso de que el usuario pertenezca a otro tenant
				// del que quiere eliminar....
				throw new ResponseStatusException(HttpStatus.FORBIDDEN,
						SSOMessages.ELIMINAR_USUARIO_DENEGADO.toString());
			}

			Usuario usuarioEliminado = usuarioService.delete(usuario.get(), apk);
			usuarioEliminado.setPassword("???");

			return ResponseEntity.ok(usuarioEliminado);
		} else {

			LOG.warn(SSOMessages.USUARIO_NO_ENCONTRADO.toString());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, SSOMessages.USUARIO_NO_ENCONTRADO.toString());
		}
	}

	// PUT: http://localhost:8080/Usuarios/Habilitar/1
	@RequestMapping(value = "/Habilitar/{idUsuario}", method = RequestMethod.PUT)
	public ResponseEntity<Usuario> habilitarUsuario(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") long idUsuario) throws Exception {

		Optional<Usuario> usuario = usuarioService.findById(idUsuario);

		if (usuario != null && usuario.isPresent()) {
			if (!usuario.get().getTenant().getApiKey().equals(apk.trim())) {
				LOG.warn(SSOMessages.HABILITAR_USUARIO_DENEGADO.toString());
				// validacion en caso de que el usuario pertenezca a otro tenant
				// del que quiere habilitar....
				throw new ResponseStatusException(HttpStatus.FORBIDDEN,
						SSOMessages.HABILITAR_USUARIO_DENEGADO.toString());
			}

			Usuario usuarioHabilitado = usuarioService.activate(usuario.get(), apk);
			usuarioHabilitado.setPassword("???");

			return ResponseEntity.ok(usuarioHabilitado);
		} else {
			LOG.warn(SSOMessages.USUARIO_NO_ENCONTRADO.toString());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, SSOMessages.USUARIO_NO_ENCONTRADO.toString());
		}
	}
}
