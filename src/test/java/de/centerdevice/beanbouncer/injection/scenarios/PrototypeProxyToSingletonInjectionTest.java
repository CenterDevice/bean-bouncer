package de.centerdevice.beanbouncer.injection.scenarios;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.centerdevice.beanbouncer.BeanBouncer;
import de.centerdevice.beanbouncer.common.CommonTestConfiguration;
import de.centerdevice.beanbouncer.common.InnerClass;
import de.centerdevice.beanbouncer.common.OuterClass;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonTestConfiguration.class, PrototypeProxyToSingletonInjectionTest.class,
		BeanBouncer.class })
public class PrototypeProxyToSingletonInjectionTest extends TestCase {

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
	InnerClass getInnerClass() {
		return new InnerClass();
	}

	@Bean
	@Lazy
	OuterClass getOuterClass(InnerClass innerClass) {
		return new OuterClass(innerClass);
	}

	@Autowired
	ConfigurableListableBeanFactory beanFactory;

	@Test
	public void testBeanCreation() {
		beanFactory.getBean(OuterClass.class);
	}
}
