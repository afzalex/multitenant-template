package com.example.multitenancy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.multitenancy.entity.DataSourceConfig;

public interface DataSourceConfigRepository extends JpaRepository<DataSourceConfig, Long> {
	DataSourceConfig findByTenantName(String tenantName);
}
