package com.sso.springboot.JWT;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.sso.springboot.Messages.SSOMessages;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	private static final Logger LOG = LoggerFactory.getLogger(JwtRequestFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");
		String requestKey = request.getHeader("x-api-key");

		String userId = null;
		String jwtToken = null;

		if ((requestKey != null)&&(requestTokenHeader==null)) {
			
			LOG.info("Se ignora la validacion por jwt la llamada es desde un tenant");
			chain.doFilter(request, response);
			
		} else {

			if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
				jwtToken = requestTokenHeader.substring(7);

				try {
					userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
				} catch (IllegalArgumentException e) {
					LOG.warn(SSOMessages.JWT_ERROR_OBTENER_JWT.toString());

					throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
							SSOMessages.JWT_ERROR_OBTENER_JWT.toString());

				} catch (ExpiredJwtException e) {

					LOG.warn(SSOMessages.JWT_EXPIRADO.toString());

					//throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, SSOMessages.JWT_EXPIRADO.toString());

				}
			} else {
				LOG.warn(SSOMessages.JWT_BARRER_MISSING.toString());

				//throw new ResponseStatusException(HttpStatus.BAD_REQUEST, SSOMessages.JWT_BARRER_MISSING.toString());

			}

			// Once we get the token validate it.
			if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(userId);

				// if token is valid configure Spring Security to manually set
				// authentication
				if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					// After setting the Authentication in the context, we
					// specify
					// that the current user is authenticated. So it passes the
					// Spring Security Configurations successfully.
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
			chain.doFilter(request, response);
		}
	}
}