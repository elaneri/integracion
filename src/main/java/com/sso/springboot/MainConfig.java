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
			dbUri = new URI("    postgres://vkrwnqnuwnvaou:e1f53a7756d8c3e1ab9a9e29791720bcce1b5e3edbe8abbd272a5e48ed192987@ec2-54-197-254-117.compute-1.amazonaws.com:5432/d2ri9a64s9fttp");
			
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
