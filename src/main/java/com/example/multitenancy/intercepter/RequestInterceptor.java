package com.example.multitenancy.intercepter;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.multitenancy.annotation.TenantSpecific;
import com.example.multitenancy.bean.TenantContext;
import com.example.multitenancy.config.MultiTenantConnectionProviderImpl;
import com.example.multitenancy.constants.MultitenancyConstants;
import com.example.multitenancy.exception.InvalidTenantIdException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RequestInterceptor implements AsyncHandlerInterceptor {

	@Autowired
	private MultiTenantConnectionProviderImpl multiTenantConnectionProvider;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.trace("HTTP Request is intercepted");

		if (handler instanceof HandlerMethod) {
			Method method = ((HandlerMethod) handler).getMethod();
			TenantSpecific tenantSpecific = method.getAnnotation(TenantSpecific.class);
			if (tenantSpecific == null) {
				tenantSpecific = method.getDeclaringClass().getAnnotation(TenantSpecific.class);
			}
			if (tenantSpecific != null) {
				String requestURI = request.getRequestURI();
				String tenantID = request.getHeader(MultitenancyConstants.TENANT_HEADER_KEY);
				if (multiTenantConnectionProvider.isTenantActive(tenantID)) {
					log.debug("RequestURI:" + requestURI);
					log.debug(String.format("%s : %s", MultitenancyConstants.TENANT_HEADER_KEY, tenantID));
					TenantContext.setCurrentTenant(tenantID);
				} else {
					log.debug("Invalid/Inactive tenantID passed : " + tenantID);
					throw new InvalidTenantIdException(tenantID);
				}
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		TenantContext.clear();
	}

}