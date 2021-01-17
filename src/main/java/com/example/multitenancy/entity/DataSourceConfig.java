package com.example.multitenancy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@Entity
@Table(name = "DataSourceConfig")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataSourceConfig implements Serializable {
	private static final long serialVersionUID = 5104181924076372196L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "tenant_name")
	private String tenantName;
	private String url;
	private String username;
	private String password;

	@Column(name = "driverclassname")
	private String driverClassName;

	@Column(name = "is_active")
	private boolean isActive;

	@Column(name = "is_abstract")
	private boolean isAbstract;

	@Column(name = "is_local")
	private boolean isLocal;
}