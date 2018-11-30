package de.centerdevice.beanbouncer;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import de.centerdevice.beanbouncer.handler.LoggingUnsafeInjectionHandler;
import de.centerdevice.beanbouncer.handler.UnsafeInjectionHandler;
import de.centerdevice.beanbouncer.provider.AnnotationSafeTargetScopeProvider;
import de.centerdevice.beanbouncer.provider.CachedSafeTargetScopeProvider;
import de.centerdevice.beanbouncer.provider.SafeTargetScopeProvider;
import de.centerdevice.beanbouncer.whitelist.BeanWhitelist;

public class BeanBouncer implements BeanPostProcessor {
	private final Logger logger = LoggerFactory.getLogger(BeanBouncer.class);

	private final ConfigurableListableBeanFactory configurableBeanFactory;
	private final UnsafeInjectionHandler unsafeInjectionHandler;
	private final Optional<BeanWhitelist> whitelist;

	private final SafeTargetScopeProvider safeTargetScopeProvider = new CachedSafeTargetScopeProvider(
			new AnnotationSafeTargetScopeProvider());

	private final Set<String> verifiedBeanNames = new HashSet<>();

	public BeanBouncer(ConfigurableListableBeanFactory beanFactory,
			Optional<UnsafeInjectionHandler> unsafeInjectionHandler, Optional<BeanWhitelist> whitelist) {
		this.configurableBeanFactory = beanFactory;
		this.whitelist = whitelist;
		this.unsafeInjectionHandler = unsafeInjectionHandler.orElseGet(() -> new LoggingUnsafeInjectionHandler());
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (verifiedBeanNames.contains(beanName) || whitelist.map(w -> w.isWhitelisted(beanName)).orElse(false)) {
			return bean;
		}

		try {
			verifyBean(beanName);

			verifiedBeanNames.add(beanName);
		} catch (NoSuchBeanDefinitionException e) {
			logger.warn("Cannot find definition for bean '{}'.", beanName);
		}

		return bean;
	}

	private void verifyBean(String beanName) {
		String beanScope = configurableBeanFactory.getBeanDefinition(beanName).getScope();
		String[] dependenciesForBean = configurableBeanFactory.getDependenciesForBean(beanName);

		InjectionScopeVerifier injectionScopeVerifier = new InjectionScopeVerifier(beanName, beanScope,
				safeTargetScopeProvider, unsafeInjectionHandler);

		for (String dependencyBeanName : dependenciesForBean) {
			if (!isAopProxy(dependencyBeanName)) {
				injectionScopeVerifier.verifyDependency(dependencyBeanName,
						configurableBeanFactory.getBeanDefinition(dependencyBeanName));
			}
		}
	}

	private boolean isAopProxy(String dependencyBeanName) {
		return AopUtils.isAopProxy(configurableBeanFactory.getBean(dependencyBeanName));
	}
}
