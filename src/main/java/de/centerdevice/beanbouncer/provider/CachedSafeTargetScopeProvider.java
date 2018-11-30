package de.centerdevice.beanbouncer.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;

public class CachedSafeTargetScopeProvider implements SafeTargetScopeProvider {

	private final Map<String, Set<String>> cache = new HashMap<>();

	private final SafeTargetScopeProvider delegate;

	public CachedSafeTargetScopeProvider(SafeTargetScopeProvider delegate) {
		this.delegate = delegate;
	}

	@Override
	public Set<String> getSafeTargetScopes(String beanName, BeanDefinition beanDefinition) {
		return cache.computeIfAbsent(beanName, (name) -> delegate.getSafeTargetScopes(name, beanDefinition));
	}

}
