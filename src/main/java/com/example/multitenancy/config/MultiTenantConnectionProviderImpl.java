package com.example.multitenancy.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.example.multitenancy.bean.TenantDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

	private static final long serialVersionUID = -6610394541904773172L;

	public static final String DEFAULT_TENANT_ID = "pubilc";

	@Autowired
	private DataSource defaultDS;

	@Autowired
	private ApplicationContext context;

	private final Map<String, DataSource> map = new HashMap<>();
	private final List<DataSource> tenantDataSources = new ArrayList<>();
	private final List<DataSource> unmodifiableTenantDataSourcesList = Collections.unmodifiableList(tenantDataSources);

	boolean init = false;

	@PostConstruct
	public void reset() {
		tenantDataSources.clear();
		map.clear();
		map.put(DEFAULT_TENANT_ID, defaultDS);
	}

	@Override
	protected DataSource selectAnyDataSource() {
		return map.get(DEFAULT_TENANT_ID);
	}

	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		init();
		return map.get(tenantIdentifier) != null ? map.get(tenantIdentifier) : map.get(DEFAULT_TENANT_ID);
	}

	public List<DataSource> getAllDataSources() {
		init();
		return new ArrayList<>(map.values());
	}

	public List<DataSource> getAllTenantDataSources() {
		init();
		return unmodifiableTenantDataSourcesList;
	}

	public boolean isTenantActive(String tenant) {
		return tenant != null && map.containsKey(tenant);
	}

	private void init() {
		if (!init) {
			init = true;
			TenantDataSource tenantDataSource = context.getBean(TenantDataSource.class);
			reset();
			tenantDataSource.getAll().entrySet().parallelStream().forEach(e -> {
				try {
					Connection conn = e.getValue().getConnection();
					if (conn.createStatement().executeQuery("SELECT 1").next()) {
						tenantDataSources.add(e.getValue());
						map.put(e.getKey(), e.getValue());
					}
				} catch (SQLException e1) {
					if (log.isDebugEnabled()) {
						log.debug("Unable to make connection for " + e.getKey());
					} else {
						log.trace("Unable to make connection for " + e.getKey(), e1);
					}
				}
			});
		}
	}
}