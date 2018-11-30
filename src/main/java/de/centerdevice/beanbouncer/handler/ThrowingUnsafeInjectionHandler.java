package de.centerdevice.beanbouncer.handler;

public class ThrowingUnsafeInjectionHandler implements UnsafeInjectionHandler {

	@Override
	public void handleUnsafeInjection(String beanName, String beanScope, String dependencyBeanName,
			String dependencyBeanScope) {
		throw new IllegalStateException("Cannot safely inject " + dependencyBeanScope + " scoped bean '"
				+ dependencyBeanName + "' into " + beanScope + " scoped bean '" + beanName + "'");
	}

}
