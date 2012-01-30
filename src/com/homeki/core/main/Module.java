package com.homeki.core.main;

public interface Module {
	void construct(ConfigurationFile file);
	void destruct();
}
