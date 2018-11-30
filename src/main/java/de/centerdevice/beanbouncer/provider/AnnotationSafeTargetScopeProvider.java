package de.centerdevice.beanbouncer.provider;

import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.type.AnnotatedTypeMetadata;

import de.centerdevice.beanbouncer.annotation.InjectableInto;

public class AnnotationSafeTargetScopeProvider implements SafeTargetScopeProvider {

	@Override
	public Set<String> getSafeTargetScopes(String beanName, BeanDefinition beanDefinition) {
		if (beanDefinition instanceof AnnotatedBeanDefinition) {
			return getSafeTargetScopes((AnnotatedBeanDefinition) beanDefinition);
		}
		return Collections.emptySet();
	}

	private Set<String> getSafeTargetScopes(AnnotatedBeanDefinition annotatedBeanDefinition) {
		Set<String> beanTargetScopes = getAnnotatedTargetScopes(annotatedBeanDefinition.getMetadata());
		Set<String> factoryTargetScopes = getAnnotatedTargetScopes(annotatedBeanDefinition.getFactoryMethodMetadata());

		HashSet<String> allScopes = new HashSet<>(beanTargetScopes);
		allScopes.addAll(factoryTargetScopes);
		return allScopes;
	}

	private Set<String> getAnnotatedTargetScopes(AnnotatedTypeMetadata factoryMethodMetadata) {
		Map<String, Object> annotationAttributes = factoryMethodMetadata
				.getAnnotationAttributes(InjectableInto.class.getName(), true);
		if (annotationAttributes == null) {
			return Collections.emptySet();
		}

		return annotationAttributes.values().stream().filter(v -> v instanceof String[])
				.flatMap(v -> Arrays.stream((String[]) v)).collect(toSet());
	}
}
