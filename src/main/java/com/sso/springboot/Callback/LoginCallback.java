package com.sso.springboot.Callback;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.sso.springboot.JWT.JwtTokenUtil;
import com.sso.springboot.Messages.SSOMessages;
import com.sso.springboot.Tenant.Tenant;
import com.sso.springboot.Tenant.TenantService;
import com.sso.springboot.UserClaims.UserClaims;
import com.sso.springboot.UserClaims.UserClaimsService;
import com.sso.springboot.Usuario.Usuario;
import com.sso.springboot.Usuario.UsuarioService;

@Controller
public class LoginCallback {

	@Autowired
	private UsuarioService userService;

	@Autowired
	private UserClaimsService userClaimService;

	@Autowired
	private TenantService tenantService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	private static String CALLBACK_VALIDATOR = "/LoginCallbackValidator";

	private static final Logger LOG = LoggerFactory.getLogger(LoginCallback.class);


	@PostMapping("/LoginCallback")
	public RedirectView callBack(HttpServletRequest request, @RequestParam("usuario") String usuario,
			@RequestParam("password") String password, @RequestParam("tenant") String tenant) throws Exception {
			
		
		LOG.info("tenant callback = " + tenant);
		
		// write your code to save details
		Tenant tn = tenantService.findByApiName(tenant);
		String url = "";
		
		try {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(usuario, password);

			authToken.setDetails(tn.getApiKey());

			authenticationManager.authenticate(authToken);

			/* esta parte se ejecuta si authenticationManager esta ok */
			Optional<Usuario> user = userService.findByUserNameAndTenant(usuario, tn.getApiKey());

			Map<String, Object> claims = new HashMap<>();
			List<UserClaims> userClaims = userClaimService.findClaimsForUser(user.get());

			for (UserClaims c : userClaims) {
				claims.put(c.getClaim().getNombre(), c.getClaimValue());
			}

			claims.put("client_id", user.get().getIdUsuario());
			claims.put("iss", tn.getNombre());
			String idUsuario = String.valueOf(user.get().getIdUsuario());

			final String token = jwtTokenUtil.generateToken(idUsuario, claims);

			request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
			url = ((tn.getCallbackSuccess() == null) ? CALLBACK_VALIDATOR : tn.getCallbackSuccess());
			url += "?TOKEN=" + token;

			LOG.info("Token generado para usuario = " + usuario);

		} catch (DisabledException e) {
			url = ((tn.getCallbackError() == null) ? CALLBACK_VALIDATOR : tn.getCallbackError());
			url += "?ERROR=" + toURI(SSOMessages.USUARIO_INVALIDO.toString());
			LOG.info(e.getMessage());


		} catch (BadCredentialsException e) {
			url = ((tn.getCallbackError() == null) ? CALLBACK_VALIDATOR : tn.getCallbackError());
			url += "?ERROR=" + toURI(SSOMessages.USUARIO_INVALIDO.toString());
			LOG.info(e.getMessage());
		}

		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(url);

		return redirectView;
	}

	public String toURI(String val) {
		try {
			return URLEncoder.encode(val, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex.getCause());
		}
	}
}
