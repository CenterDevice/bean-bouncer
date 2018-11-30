package de.centerdevice.beanbouncer.common;

import org.springframework.context.annotation.Bean;

import de.centerdevice.beanbouncer.handler.ThrowingUnsafeInjectionHandler;
import de.centerdevice.beanbouncer.handler.UnsafeInjectionHandler;

public class CommonTestConfiguration {
	@Bean
	UnsafeInjectionHandler getUnsafeInjectionHandler() {
		return new ThrowingUnsafeInjectionHandler();
	}
}
