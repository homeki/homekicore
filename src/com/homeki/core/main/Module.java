package com.homeki.core.main;

public interface Module {
	void construct(Monitor monitor, ConfigurationFile file);
	void destruct();
}
