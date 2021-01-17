package com.example.multitenancy.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlyWayConfig {

	@Autowired
	private final MultiTenantConnectionProviderImpl connectionProvider;

	public FlyWayConfig(MultiTenantConnectionProviderImpl connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

	@PostConstruct
	public void migrate() {
		for (Object dataSource : connectionProvider.getAllTenantDataSources()) {
			DataSource source = (DataSource) dataSource;
			Flyway flyway = Flyway.configure().locations("db/tenant/migration").dataSource(source).load();
			flyway.migrate();
		}
	}

}