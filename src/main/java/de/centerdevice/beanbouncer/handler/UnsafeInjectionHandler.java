package de.centerdevice.beanbouncer.handler;

public interface UnsafeInjectionHandler {
	void handleUnsafeInjection(String beanName, String beanScope, String dependencyBeanName,
			String dependencyBeanScope);
}
