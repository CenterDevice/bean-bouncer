package de.centerdevice.beanbouncer.injection.scenarios;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.centerdevice.beanbouncer.common.CommonTestConfiguration;
import de.centerdevice.beanbouncer.common.InnerClass;
import de.centerdevice.beanbouncer.common.OuterClass;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonTestConfiguration.class, SingletonToPrototypeInjectionTest.class })
public class SingletonToPrototypeInjectionTest extends TestCase {

	@Bean
	InnerClass getInnerClass() {
		return new InnerClass();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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
