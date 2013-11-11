package com.homeki.core.main;

import java.io.File;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;

import com.homeki.core.logging.L;

public class WebGuiModule implements Module {
	private Component webGuiRestletComponent;
	
	@Override
	public void construct() {
		File webRoot = new File(Configuration.WEBROOT_PATH);
		if (webRoot.exists()) {
			L.i("Web root exists, starting static web server on port 8080.");
			try {
				webGuiRestletComponent = new Component();
				webGuiRestletComponent.getServers().add(Protocol.HTTP, 8080);
				webGuiRestletComponent.getClients().add(Protocol.FILE);
				webGuiRestletComponent.getDefaultHost().attach(new Directory(webGuiRestletComponent.getContext().createChildContext(), "file://" + Configuration.WEBROOT_PATH));
				webGuiRestletComponent.start();
			} catch (Exception e) {
				L.e("Unknown exception when starting static web server on port 8080.", e);
			}
		}
		else {
			L.i("Found no web root in " + Configuration.WEBROOT_PATH + ", skipping start of static web server.");
		}
	}

	@Override
	public void destruct() {
		if (webGuiRestletComponent != null) {
			try {
				webGuiRestletComponent.stop();
			} catch (Exception e) {
				L.e("Failed to stop web GUI restlet HTTP server.", e);
			}
		}
	}
}
