package com.homeki.core.web;

import com.homeki.core.logging.L;
import com.homeki.core.main.Module;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.servlet.ServletContainer;

public class WebModule implements Module {
	private Tomcat tomcat;

  @Override
  public void construct() {
    tomcat = new Tomcat();
    tomcat.setPort(5001);
    tomcat.setBaseDir("");

		Context ctx = tomcat.addContext("", "/tmp");

		try {
			Wrapper wrapper = tomcat.addServlet(ctx, "SomeServlet", new ServletContainer());
			wrapper.addInitParameter("javax.ws.rs.Application", "com.homeki.core.web.JerseyApplication");
			wrapper.addMapping("/api/*");

			tomcat.start();
		} catch (Exception e) {
			L.e("Failed to start WebModule.", e);
		}
  }

  @Override
  public void destruct() {
		try {
			tomcat.stop();
		} catch (LifecycleException e) {
			L.e("Failed to stop REST API module.", e);
		}
	}
}
