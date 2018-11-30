package de.centerdevice.beanbouncer.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.centerdevice.beanbouncer.BeanBouncer;

public class LoggingUnsafeInjectionHandler implements UnsafeInjectionHandler {

	private final Logger logger = LoggerFactory.getLogger(BeanBouncer.class);

	@Override
	public void handleUnsafeInjection(String beanName, String beanScope, String dependencyBeanName,
			String dependencyBeanScope) {
		logger.warn("Cannot safely inject {} scoped bean '{}' into {} scoped bean '{}'", dependencyBeanScope,
				dependencyBeanName, beanScope, beanName);
	}

}
