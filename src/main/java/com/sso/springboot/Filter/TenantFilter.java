package com.sso.springboot.Filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sso.springboot.Messages.JWTError;
import com.sso.springboot.Tenant.Tenant;
import com.sso.springboot.Tenant.TenantServiceImpl;

@Component
public class TenantFilter extends OncePerRequestFilter {

	@Autowired
	private TenantServiceImpl tenantService;

	
	@Value("${PUBLIC_LOGIN_URL}")
	private String calbackurl;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		final String requestKey = request.getHeader("x-api-key");

		String url = ((HttpServletRequest) request).getRequestURL().toString();
		
		if (url.indexOf(calbackurl)>0){
			
			logger.warn("Callback submit" );
			chain.doFilter(request, response);
			
		}else{
			Tenant t = tenantService.findByApikey(requestKey);

			if (t != null) {

				logger.warn("valid key for tenant = " + t.getNombre());
				chain.doFilter(request, response);

			} else {
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				response.getWriter().write(JWTError.TENANT_API_KEY.toString());
			}
		}
		
		
		
	}

}
