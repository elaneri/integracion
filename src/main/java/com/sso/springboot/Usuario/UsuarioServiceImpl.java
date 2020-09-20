package com.sso.springboot.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sso.springboot.Claims.Claim;
import com.sso.springboot.Claims.ClaimsDAO;
import com.sso.springboot.Claims.ClaimsServiceImpl;
import com.sso.springboot.Tenant.Tenant;
import com.sso.springboot.Tenant.TenantServiceImpl;
import com.sso.springboot.UserClaims.UserClaims;
import com.sso.springboot.UserClaims.UserClaimsDAO;

@Service
@Transactional(readOnly = false)
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	UsuarioDAO usuarioDAO;

	@Autowired
	ClaimsDAO claimsDAO;



	@Autowired
	private TenantServiceImpl tenantService;


	@Transactional(readOnly = true)
	public Optional<Usuario> findById(Long id) {
		return usuarioDAO.findById(id);
	}

	@Override
	public Usuario save(Usuario usuario, String apk) {

		Tenant t = tenantService.findByApikey(apk);
		usuario.setTenant(t);

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		// genero hash con Bcrypt
		String encodedPassword = passwordEncoder.encode(usuario.getPassword());
		usuario.setPassword(encodedPassword);		
		Usuario us = usuarioDAO.save(usuario);

		return us;
	}

	@Override
	public Usuario update(Usuario usuarioExistente, Usuario usuarioModificado, String apk) {
		
		usuarioExistente.setNombre(usuarioModificado.getNombre().trim());
		usuarioExistente.setApellido(usuarioModificado.getApellido().trim());
		usuarioExistente.setPassword(usuarioModificado.getPassword().trim());
		usuarioExistente.setMail(usuarioModificado.getMail().trim());
		usuarioExistente.setFecha_nacimiento(usuarioModificado.getFecha_nacimiento().trim());
		usuarioExistente.setTelefono(usuarioModificado.getTelefono().trim());
		usuarioExistente.setPropiedades(usuarioModificado.getPropiedades());
		
		return this.save(usuarioExistente, apk);
	}

	@Transactional(readOnly = true)
	public Optional<Usuario> findByUserAndPassAndApiKey(String user, String pass, String apiKey) {

		Optional<Usuario> usuario = usuarioDAO.findByUsuario(user);
		// En la rutina hago el match del texto plano con la pass de la db (dado
		// que no se puede comparar directamente de la BD)
		if (usuario.isPresent() && new BCryptPasswordEncoder().matches(pass, usuario.get().getPassword().trim())
				&& usuario.get().getTenant().getApiKey().equals(apiKey) && usuario.get().isEnable()) {
			return usuario;
		} else {
			return null;
		}
	}

	@Override
	public Optional<Usuario> findByUserName(String user) {

		Optional<Usuario> usuario = usuarioDAO.findByUsuario(user);

		if (usuario != null && usuario.isPresent() && usuario.get().isEnable()) {
			return usuario;
		} else {
			return null;
		}
	}
}
