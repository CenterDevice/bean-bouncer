package de.centerdevice.beanbouncer.utils;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.springframework.core.type.AnnotatedTypeMetadata;

import de.centerdevice.beanbouncer.annotation.InjectableInto;

public final class InjectableIntoUtil {
	public static void allowInjectionInto(AnnotatedTypeMetadata metadata, String[] value) {
		when(metadata.getAnnotationAttributes(eq(InjectableInto.class.getName()), eq(true)))
				.thenReturn(injectableInto(value));
	}

	private static HashMap<String, Object> injectableInto(String[] value) {
		HashMap<String, Object> annotationValues = new HashMap<>();
		annotationValues.put("", value);
		return annotationValues;
	}
}
