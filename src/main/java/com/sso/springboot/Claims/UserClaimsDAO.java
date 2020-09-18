package com.sso.springboot.Claims;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserClaimsDAO extends JpaRepository<UserClaims, Long> {

}
