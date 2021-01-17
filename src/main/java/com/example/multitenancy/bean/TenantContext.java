package com.example.multitenancy.bean;

import lombok.Data;

public class TenantContext {

	public static final String DEFAULT_TENANT = "public";

	private static ThreadLocal<TenantHolder> currentTenantHolder = new InheritableThreadLocal<>();

	public static String getCurrentTenant() {
		TenantHolder tenantHolder = currentTenantHolder.get();
		return tenantHolder == null ? null : tenantHolder.isTenantSpecificActive ? tenantHolder.tenant : DEFAULT_TENANT;
	}

	public static void setCurrentTenant(String tenant) {
		currentTenantHolder.set(TenantHolder.of(tenant));
	}

	public static void clear() {
		currentTenantHolder.set(null);
	}

	public static void switchToTenantSpecific() {
		TenantHolder tenantHolder = currentTenantHolder.get();
		if (tenantHolder != null) {
			tenantHolder.setTenantSpecificActive(true);
		}
	}

	public static void switchToGlobal() {
		TenantHolder tenantHolder = currentTenantHolder.get();
		if (tenantHolder != null) {
			tenantHolder.setTenantSpecificActive(false);
		}
	}

	@Data
	private static class TenantHolder {
		private final String tenant;
		private boolean isTenantSpecificActive = true;

		private static TenantHolder of(String tenant) {
			return new TenantHolder(tenant);
		}
	}
}