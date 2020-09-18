package com.sso.springboot.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sso.springboot.Tenant.Tenant;

@Service
@Transactional(readOnly = false)
public class ClaimsServiceImpl implements ClaimsService {

	@Autowired
	ClaimsDAO claimDAO;

	@Override
	public Claim findByNombre(String nombre) {
		return claimDAO.findByNombre(nombre);
	}

}
