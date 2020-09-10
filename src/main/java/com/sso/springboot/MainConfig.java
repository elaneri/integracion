package com.sso.springboot;

import java.net.URI;
import java.net.URISyntaxException;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfig {

	@Bean
	public DataSource getDataSource() {
		URI dbUri;
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();

		try {
			
			dbUri = new URI(System.getenv("JDBC_DATABASE_URL"));
			
			String username = dbUri.getUserInfo().split(":")[0];
			String password = dbUri.getUserInfo().split(":")[1];
			String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

			dataSourceBuilder.driverClassName("org.postgresql.Driver");
			dataSourceBuilder.url(dbUrl);
			dataSourceBuilder.username(username);
			dataSourceBuilder.password(password);

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataSourceBuilder.build();
	}

}
