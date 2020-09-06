package com.sso.springboot.Tenant;

import java.util.Optional;

public interface TenantService {
	
	public Optional<Tenant> findById(Long id);
	
}
