package com.sso.springboot.Tenant;


public  interface TenantService {
	public void save(Tenant tenant);
	public Tenant findByApikey(String apiKey);
	public Tenant findByApiName(String tenant);

}
