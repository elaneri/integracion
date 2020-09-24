package com.sso.springboot.callback;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.sso.springboot.JWT.JwtTokenUtil;
import com.sso.springboot.Messages.JWTError;
import com.sso.springboot.Messages.JWTMessages;
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

	// @Value("${CALLBACK_VALIDATOR}")
	private static String CALLBACK_VALIDATOR = "/LoginCallbackValidator";

	@PostMapping("/LoginCallback")
	public RedirectView saveDetails(HttpServletRequest request, @RequestParam("usuario") String usuario,
			@RequestParam("password") String password, @RequestParam("tenant") String tenant, ModelMap modelMap)
			throws Exception {
		// write your code to save details
		Tenant tn = tenantService.findByApiName(tenant);
		String Error = "";
		String url = "";
		try {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(usuario, password);

			authToken.setDetails(tn.getApiKey());

			authenticationManager.authenticate(authToken);

			/* esta parte se ejecuta si authenticationManager esta ok */
			Optional<Usuario> user = userService.findByUserName(usuario);

			Map<String, Object> claims = new HashMap<>();
			List<UserClaims> userClaims = userClaimService.findClaimsForUser(user.get());

			for (UserClaims c : userClaims) {

				claims.put(c.getClaim().getNombre(), c.getClaimValue());

			}
			claims.put("client_id", user.get().getIdUsuario());
			claims.put("iss", tn.getNombre());

			final String token = jwtTokenUtil.generateToken(user.get().getUsuario(), claims);

			request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);

			url = ((tn.getCallbackSuccess()== null) ? CALLBACK_VALIDATOR : tn.getCallbackSuccess());
			url += "?TOKEN=" + token;

		} catch (DisabledException e) {
			url = ((tn.getCallbackError() == null) ? CALLBACK_VALIDATOR : tn.getCallbackError());
			url += "?ERROR=" + toURI(JWTError.USUARIO_INVALIDO.toString());

		} catch (BadCredentialsException e) {
			url = ((tn.getCallbackError() == null) ? CALLBACK_VALIDATOR : tn.getCallbackError());
			url += "?ERROR=" + toURI(JWTError.USUARIO_INVALIDO.toString());

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
