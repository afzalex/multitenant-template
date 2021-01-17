package com.example.multitenancy.bean;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import com.example.multitenancy.entity.DataSourceConfig;
import com.example.multitenancy.repository.DataSourceConfigRepository;

@Component
public class TenantDataSource implements Serializable {

	private static final long serialVersionUID = -9175579859396205681L;

	private HashMap<String, DataSource> dataSources = new HashMap<>();

	@Autowired
	private DataSourceConfigRepository configRepo;

	public DataSource getDataSource(String name) {
		if (dataSources.get(name) != null) {
			return dataSources.get(name);
		}
		DataSource dataSource = createDataSource(name);
		if (dataSource != null) {
			dataSources.put(name, dataSource);
		}
		return dataSource;
	}

	@PostConstruct
	public Map<String, DataSource> getAll() {
		List<DataSourceConfig> configList = configRepo.findAll();
		Map<String, DataSource> result = new HashMap<>();
		for (DataSourceConfig config : configList) {
			DataSource dataSource = getDataSource(config.getTenantName());
			result.put(config.getTenantName(), dataSource);
		}
		return result;
	}

	private DataSource createDataSource(String name) {
		DataSourceConfig config = configRepo.findByTenantName(name);
		if (config != null) {
			DataSourceBuilder<?> factory = DataSourceBuilder.create().driverClassName(config.getDriverClassName())
					.username(config.getUsername()).password(config.getPassword()).url(config.getUrl());
			DataSource ds = factory.build();
			return ds;
		}
		return null;
	}

	@PreDestroy
	public void destroy() {
		dataSources.forEach((String name, DataSource datasource) -> {
			try {
				datasource.getConnection().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}
}