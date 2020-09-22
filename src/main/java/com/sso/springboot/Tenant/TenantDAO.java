package com.sso.springboot.Tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface TenantDAO extends JpaRepository<Tenant, Long> {

	
	@Query("select t from Tenant t where t.api_key =?1 and fecha_baja is  null")
	Tenant findByApyKey(String apyKey);
	
	@Query("select t from Tenant t where t.nombre =?1 and fecha_baja is  null")
	Tenant findByApyName(String nombre);
	
}
