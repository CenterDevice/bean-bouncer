package de.centerdevice.beanbouncer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import de.centerdevice.beanbouncer.handler.UnsafeInjectionHandler;
import de.centerdevice.beanbouncer.provider.SafeTargetScopeProvider;

public class InjectionScopeVerifier {

	private final Logger logger = LoggerFactory.getLogger(InjectionScopeVerifier.class);

	private final String beanName;
	private final String beanScope;

	private final UnsafeInjectionHandler unsafeInjectionHandler;
	private final SafeTargetScopeProvider safeTargetScopeProvider;

	public InjectionScopeVerifier(String beanName, String beanScope, SafeTargetScopeProvider safeTargetScopeProvider,
			UnsafeInjectionHandler unsafeInjectionHandler) {
		this.beanName = beanName;
		this.beanScope = beanScope;

		this.safeTargetScopeProvider = safeTargetScopeProvider;
		this.unsafeInjectionHandler = unsafeInjectionHandler;
	}

	public void verifyDependency(String dependencyBeanName, BeanDefinition dependencyBeanDefinition) {
		String dependencyBeanScope = dependencyBeanDefinition.getScope();

		if (dependencyBeanScope == null) {
			logger.warn("Cannot determine scope of bean '{}' for injection into '{}'.", dependencyBeanName, beanName);
		} else if (!canSafelyInjectInto(dependencyBeanName, dependencyBeanScope, dependencyBeanDefinition, beanScope)) {
			unsafeInjectionHandler.handleUnsafeInjection(beanName, beanScope, dependencyBeanName, dependencyBeanScope);
		}
	}

	private boolean canSafelyInjectInto(String dependencyBeanName, String dependencyBeanScope,
			BeanDefinition dependencyBeanDefinition, String beanScope) {
		if (isSameScope(dependencyBeanScope, beanScope) || isPrototype(beanScope) || isSingleton(dependencyBeanScope)
				|| isSafeScopeTarget(dependencyBeanName, dependencyBeanDefinition, beanScope)) {
			return true;
		}

		return false;
	}

	private boolean isSingleton(String scope) {
		return ConfigurableBeanFactory.SCOPE_SINGLETON.equals(getEffectiveScope(scope));
	}

	private boolean isPrototype(String scope) {
		return ConfigurableBeanFactory.SCOPE_PROTOTYPE.equals(scope);
	}

	private boolean isSafeScopeTarget(String dependencyBeanName, BeanDefinition dependencyBeanDefinition,
			String beanScope) {
		return safeTargetScopeProvider.getSafeTargetScopes(dependencyBeanName, dependencyBeanDefinition)
				.contains(getEffectiveScope(beanScope));
	}

	private boolean isSameScope(String dependencyBeanScope, String beanScope) {
		return getEffectiveScope(dependencyBeanScope).equals(getEffectiveScope(beanScope));
	}

	private String getEffectiveScope(String scope) {
		return ("".equals(scope)) ? ConfigurableBeanFactory.SCOPE_SINGLETON : scope;
	}
}
