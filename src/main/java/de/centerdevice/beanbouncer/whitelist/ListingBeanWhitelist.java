package de.centerdevice.beanbouncer.whitelist;

import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ListingBeanWhitelist implements BeanWhitelist {

	private final Set<String> beanNames = new HashSet<>();

	public ListingBeanWhitelist(String... beanNames) {
		addBeanNames(beanNames);
	}

	public void addBeanNames(Collection<String> beanNames) {
		this.beanNames.addAll(beanNames);
	}

	public void addBeanNames(String... beanNames) {
		addBeanNames(asList(beanNames));
	}

	@Override
	public boolean isWhitelisted(String beanName) {
		return beanNames != null && beanNames.contains(beanName);
	}
}
