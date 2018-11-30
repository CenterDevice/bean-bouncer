package de.centerdevice.beanbouncer.common;

import org.springframework.beans.factory.annotation.Autowired;

public class OuterClass {

	private final InnerClass innerClass;

	@Autowired
	public OuterClass(InnerClass innerClass) {
		this.innerClass = innerClass;
	}

	public InnerClass getInnerClass() {
		return innerClass;
	}
}
