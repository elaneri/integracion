package com.sso.springboot.Filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sso.springboot.Messages.SSOError;
import com.sso.springboot.Tenant.Tenant;
import com.sso.springboot.Tenant.TenantServiceImpl;


@Component
public class TenantFilter extends OncePerRequestFilter {

	@Autowired
	private TenantServiceImpl tenantService;


    private static final Logger LOG = LoggerFactory.getLogger(TenantFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String calbackurl = System.getenv("PUBLIC_LOGIN_URL");
		String requestKey = request.getHeader("x-api-key");
		String referrer = request.getHeader("referer");

		LOG.info("Callback referrer  " + referrer);

		if (referrer != null && referrer != null && referrer.indexOf(calbackurl) > 0) {
			LOG.info("Callback submit");
			chain.doFilter(request, response);
		} else {
			Tenant t = tenantService.findByApikey(requestKey);
			if (t != null) {
				LOG.info("Key Validada para Tenant  " + t.getNombre());
				chain.doFilter(request, response);
			} else {
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				response.getWriter().write(SSOError.TENANT_API_KEY.toString());
			}
		}
	}
}
