package com.sso.springboot.Tenant;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
public class TenantServiceImpl implements TenantService {
	
	@Autowired
	TenantDAO tenantDAO;
	
	@Transactional(readOnly = true)
	public Optional<Tenant> findById(Long id) {
		return tenantDAO.findById(id);
	}
}
