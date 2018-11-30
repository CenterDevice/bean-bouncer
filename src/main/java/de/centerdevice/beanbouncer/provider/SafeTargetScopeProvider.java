package de.centerdevice.beanbouncer.provider;

import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;

public interface SafeTargetScopeProvider {
	Set<String> getSafeTargetScopes(String beanName, BeanDefinition beanDefinition);
}
