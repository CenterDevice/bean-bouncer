package de.centerdevice.beanbouncer;

import static de.centerdevice.beanbouncer.utils.InjectableIntoUtil.allowInjectionInto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;

import de.centerdevice.beanbouncer.handler.ThrowingUnsafeInjectionHandler;
import de.centerdevice.beanbouncer.provider.AnnotationSafeTargetScopeProvider;

@RunWith(MockitoJUnitRunner.class)
public class InjectionScopeVerifierTest {

	private static final ThrowingUnsafeInjectionHandler UNSAFE_INJECTION_HANDLER = new ThrowingUnsafeInjectionHandler();

	public static final String BEAN_NAME = "beanName";
	public static final String DEPENDENCY_BEAN_NAME = "dependencyBeanName";
	public static final String CUSTOM_SCOPE = "customScope";

	@Mock
	AnnotationMetadata annotationMetadata;

	@Mock
	MethodMetadata methodMetadata;

	@Test
	public void testSingletonToSingleton() {
		verifyInjection(ConfigurableBeanFactory.SCOPE_SINGLETON, ConfigurableBeanFactory.SCOPE_SINGLETON);
	}

	@Test
	public void testSingletonToCustom() {
		verifyInjection(ConfigurableBeanFactory.SCOPE_SINGLETON, CUSTOM_SCOPE);
	}

	@Test
	public void testCustomToPrototype() {
		verifyInjection(CUSTOM_SCOPE, ConfigurableBeanFactory.SCOPE_PROTOTYPE);
	}

	@Test(expected = IllegalStateException.class)
	public void testPrototypeToCustom() {
		verifyInjection(ConfigurableBeanFactory.SCOPE_PROTOTYPE, CUSTOM_SCOPE);
	}

	@Test(expected = IllegalStateException.class)
	public void testCustomToSingleton() {
		verifyInjection(CUSTOM_SCOPE, ConfigurableBeanFactory.SCOPE_SINGLETON);
	}

	@Test
	public void testCustomToCustom() {
		verifyInjection(CUSTOM_SCOPE, CUSTOM_SCOPE);
	}

	@Test(expected = IllegalStateException.class)
	public void testPrototypeToSingleton() {
		verifyInjection(ConfigurableBeanFactory.SCOPE_PROTOTYPE, ConfigurableBeanFactory.SCOPE_SINGLETON);
	}

	@Test
	public void testPrototypeToSingletonWithMethodAnnotation() {
		allowInjectionInto(methodMetadata, new String[] { ConfigurableBeanFactory.SCOPE_SINGLETON });

		verifyInjection(ConfigurableBeanFactory.SCOPE_PROTOTYPE, ConfigurableBeanFactory.SCOPE_SINGLETON);
	}

	@Test
	public void testPrototypeToSingletonWithClassAnnotation() {
		allowInjectionInto(annotationMetadata, new String[] { ConfigurableBeanFactory.SCOPE_SINGLETON });

		verifyInjection(ConfigurableBeanFactory.SCOPE_PROTOTYPE, ConfigurableBeanFactory.SCOPE_SINGLETON);
	}

	@Test
	public void testPrototypeToPrototype() {
		verifyInjection(ConfigurableBeanFactory.SCOPE_PROTOTYPE, ConfigurableBeanFactory.SCOPE_PROTOTYPE);
	}

	private void verifyInjection(String dependencyScope, String targetScope) {
		InjectionScopeVerifier verifier = new InjectionScopeVerifier(BEAN_NAME, targetScope,
				new AnnotationSafeTargetScopeProvider(), UNSAFE_INJECTION_HANDLER);

		AnnotatedGenericBeanDefinition dependencyBeanDefinition = new AnnotatedGenericBeanDefinition(annotationMetadata,
				methodMetadata);
		dependencyBeanDefinition.setScope(dependencyScope);

		verifier.verifyDependency(DEPENDENCY_BEAN_NAME, dependencyBeanDefinition);
	}
}
