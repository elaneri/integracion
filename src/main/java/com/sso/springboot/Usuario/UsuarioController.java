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
	public ResponseEntity<Usuario> getUsuarioByID(@RequestHeader("x-api-key") String apik,
			@PathVariable("idUsuario") Long id) {

		Optional<Usuario> usuario = usuarioService.findById(id);
		if (usuario.isPresent()) {
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

		// TODO:codear validaciones

		ValidacionUsuarioHelper.validarUsuario(usuario);
		Usuario nuevoUsuario = usuarioService.save(usuario,apk);
		return ResponseEntity.ok(nuevoUsuario);
	}

	// PUT: http://localhost:8080/Usuarios/Eliminar/1
	@RequestMapping(value = "/Eliminar/{idUsuario}", method = RequestMethod.PUT)
	public ResponseEntity<Usuario> eliminarUsuario(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") long idUsuario) {

		Optional<Usuario> usuario = usuarioService.findById(idUsuario);

		if (usuario.isPresent()) {
			usuario.get().setEnable(false);
			return ResponseEntity.ok(usuarioService.save(usuario.get(), apk));
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	// PUT: http://localhost:8080/Usuarios/1
	@RequestMapping(value = "/{idUsuario}", method = RequestMethod.PUT)
	public ResponseEntity<Usuario> actualizarUsuario(@RequestHeader("x-api-key") String apk,
			@PathVariable("idUsuario") long idUsuario, @RequestBody Usuario nuevoUsuario) {

		Optional<Usuario> usuario = usuarioService.findById(idUsuario);

		if (usuario.isPresent()) {
			usuario.get().setMail(nuevoUsuario.getMail().trim());
			usuario.get().setTelefono(nuevoUsuario.getTelefono().trim());
			return ResponseEntity.ok(usuarioService.save(usuario.get(), apk));
		} else {
			return ResponseEntity.noContent().build();
		}
	}
}
