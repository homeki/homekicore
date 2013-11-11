package com.homeki.core.report;

import org.restlet.resource.Put;

public interface InstanceResource {
	@Put("json")
	public void store(Report report);
}
