package com.homeki.core.http;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class RestletApplication extends Application {
	@Override
	public Restlet createInboundRoot() {
		Router r = new Router(getContext().createChildContext());
		r.attach("/device/list", new RestletServerResource());
		return r;
	}
}
