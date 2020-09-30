package com.sso.springboot.Usuario;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sso.springboot.Claims.ClaimsDAO;
import com.sso.springboot.Tenant.Tenant;
import com.sso.springboot.Tenant.TenantServiceImpl;
import com.sso.springboot.Validaciones.UsuarioHelper;

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
		usuario.setEnable(true);
		Usuario user = usuarioDAO.save(usuario);

		return user;
	}
	
	@Override
	public Usuario delete(Usuario usuario, String apk) {

		Date fechaActual = new Date();
		
		usuario.setEnable(false);
		usuario.setFechaBaja(UsuarioHelper.convertirFechaAFormatoJapones(fechaActual));
	
		Usuario user = usuarioDAO.save(usuario);

		return user;
	}
	
	@Override
	public Usuario activate(Usuario usuario, String apk) {

		usuario.setEnable(true);
		usuario.setFechaBaja(null);
	
		Usuario user = usuarioDAO.save(usuario);

		return user;
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
		
		Tenant ten = tenantService.findByApikey(apiKey);
		

		Optional<Usuario> usuario = usuarioDAO.findByUsuarioAndTenant(user, ten);
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
	public Optional<Usuario> findByUserNameAndTenant(String user, String apiKey) {
		
		Tenant ten = tenantService.findByApikey(apiKey);

		Optional<Usuario> usuario = usuarioDAO.findByUsuarioAndTenant(user, ten);

		if (usuario != null && usuario.isPresent() && usuario.get().isEnable()) {
			return usuario;
		} else {
			return null;
		}
	}

	@Override
	public Optional<Usuario> findByUserIdAndTenant(String userId) {
		// TODO Auto-generated method stub
		Long usId = Long.valueOf(userId);
		return usuarioDAO.findById(usId);
	}

	
}
