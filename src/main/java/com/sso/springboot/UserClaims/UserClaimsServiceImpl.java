package com.sso.springboot.UserClaims;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sso.springboot.Claims.Claim;
import com.sso.springboot.Usuario.Usuario;

@Service
@Transactional(readOnly = false)
public class UserClaimsServiceImpl implements  UserClaimsService{

	
	@Autowired
	UserClaimsDAO userClaimsDao;

	@Override
	public List<UserClaims> findClaimsForUser(Usuario usuario) {
		// TODO Auto-generated method stub
		return userClaimsDao.findClaimsForUser(usuario);
	}

	@Override
	public Optional<UserClaims> findClaimForUser(Usuario usuario, Claim claim) {
		// TODO Auto-generated method stub
		return userClaimsDao.findClaimForUser(usuario,claim );
	}

	@Override
	public UserClaims save(UserClaims userClaim) {
		// TODO Auto-generated method stub
		return userClaimsDao.save(userClaim);
	}



}
