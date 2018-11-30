package de.centerdevice.beanbouncer.configuration;

import java.util.Optional;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import de.centerdevice.beanbouncer.BeanBouncer;
import de.centerdevice.beanbouncer.handler.UnsafeInjectionHandler;
import de.centerdevice.beanbouncer.whitelist.BeanWhitelist;

@Configuration
public class BeanBouncerDefaultConfiguration {

	@Bean
	@Order(0)
	BeanBouncer getBeanBouncer(ConfigurableListableBeanFactory beanFactory,
			Optional<UnsafeInjectionHandler> unsafeInjectionHandler, Optional<BeanWhitelist> whitelist) {
		return new BeanBouncer(beanFactory, unsafeInjectionHandler, whitelist);
	}
}
