package com.sso.springboot.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantDAO extends JpaRepository<Tenant, Long> {

}