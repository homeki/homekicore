package com.homeki.core.http;

import com.homeki.core.http.filters.CharsetFilter;
import com.homeki.core.http.filters.HibernateSessionFilter;
import com.homeki.core.logging.L;
import com.homeki.core.main.Configuration;
import com.homeki.core.main.Module;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.deploy.FilterDef;
import org.apache.catalina.deploy.FilterMap;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.File;

public class RestApiModule implements Module {
	private Tomcat tomcat;

	@Override
	public void construct() {
		tomcat = new Tomcat();

		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setURIEncoding("UTF-8");
		connector.setPort(5000);
		tomcat.getService().addConnector(connector);
		tomcat.setConnector(connector);

		try {
			Context ctx;

			if (new File(Configuration.WEBROOT_PATH).exists()) {
				L.i("Static web client found.");

				ctx = tomcat.addContext("", Configuration.WEBROOT_PATH);
				ctx.addWelcomeFile("index.html");

				Wrapper wrapper = tomcat.addServlet(ctx, "DefaultServlet", new DefaultServlet());
				wrapper.setAsyncSupported(true);
				wrapper.addInitParameter("listings", "false");
				wrapper.addMapping("/");
				wrapper.setLoadOnStartup(1);
			} else {
				L.w("No static web client found, web interface will not be available.");
				ctx = tomcat.addContext("/", "/tmp");
			}

			configureMimeMappings(ctx);

			addSessionFilter(ctx);
			addCharacterEncodingFilter(ctx);

			Wrapper wrapper = tomcat.addServlet(ctx, "JerseyServlet", new ServletContainer());
			wrapper.setAsyncSupported(true);
			wrapper.addInitParameter("javax.ws.rs.Application", JerseyApplication.class.getName());
			wrapper.addMapping("/api/*");
			wrapper.setLoadOnStartup(1);

			tomcat.start();
		} catch (Exception e) {
			L.e("Failed to start RestApiModule.", e);
		}
	}

	private void configureMimeMappings(Context ctx) {
		ctx.addMimeMapping("html", "text/html");
		ctx.addMimeMapping("htm", "text/html");
		ctx.addMimeMapping("gif", "image/gif");
		ctx.addMimeMapping("jpg", "image/jpeg");
		ctx.addMimeMapping("png", "image/png");
		ctx.addMimeMapping("js", "text/javascript");
		ctx.addMimeMapping("css", "text/css");
		ctx.addMimeMapping("pdf", "application/pdf");
	}

	private void addCharacterEncodingFilter(Context ctx) {
		FilterDef filterDef = new FilterDef();
		filterDef.setFilterName(CharsetFilter.class.getSimpleName());
		filterDef.setFilter(new CharsetFilter());
		filterDef.setAsyncSupported("true");
		ctx.addFilterDef(filterDef);

		FilterMap filterMap = new FilterMap();
		filterMap.setFilterName(CharsetFilter.class.getSimpleName());
		filterMap.addURLPattern("/*");
		ctx.addFilterMap(filterMap);
	}

	private void addSessionFilter(Context ctx) {
		FilterDef filterDef = new FilterDef();
		filterDef.setFilterName(HibernateSessionFilter.class.getSimpleName());
		filterDef.setFilter(new HibernateSessionFilter());
		filterDef.setAsyncSupported("true");
		ctx.addFilterDef(filterDef);

		FilterMap filterMap = new FilterMap();
		filterMap.setFilterName(HibernateSessionFilter.class.getSimpleName());
		filterMap.addURLPattern("/api/*");
		ctx.addFilterMap(filterMap);
	}

	@Override
	public void destruct() {
		try {
			tomcat.stop();
		} catch (LifecycleException e) {
			L.e("Failed to stop RestApiModule.", e);
		}
	}
}
