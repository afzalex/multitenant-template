package com.example.multitenancy.exception;

import com.example.multitenancy.constants.MultitenancyConstants;

public class InvalidTenantIdException extends Exception {

	private static final long serialVersionUID = -8420997504210303794L;

	private final String tenantId;

	public InvalidTenantIdException(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public String getMessage() {
		return String.format("Invalid Tenant ID provided in '%s' header : %s", MultitenancyConstants.TENANT_HEADER_KEY,
				tenantId);
	}

}
