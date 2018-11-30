package de.centerdevice.beanbouncer.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import de.centerdevice.beanbouncer.configuration.BeanBouncerDefaultConfiguration;
import de.centerdevice.beanbouncer.handler.ThrowingUnsafeInjectionHandler;
import de.centerdevice.beanbouncer.handler.UnsafeInjectionHandler;

@Import(BeanBouncerDefaultConfiguration.class)
public class CommonTestConfiguration {
	@Bean
	UnsafeInjectionHandler getUnsafeInjectionHandler() {
		return new ThrowingUnsafeInjectionHandler();
	}
}
