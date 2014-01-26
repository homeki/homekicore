package com.homeki.core.http;

import com.homeki.core.logging.L;
import com.homeki.core.main.Module;
import com.homeki.core.http.filters.HibernateSessionFilter;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.deploy.FilterDef;
import org.apache.catalina.deploy.FilterMap;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.servlet.ServletContainer;

public class RestApiModule implements Module {
	private Tomcat tomcat;

	@Override
	public void construct() {
		tomcat = new Tomcat();
		tomcat.setPort(5001);
		tomcat.setBaseDir("");

		try {
			Context ctx = tomcat.addContext("", "/tmp");

			FilterDef filterDef = new FilterDef();
			filterDef.setFilterName(HibernateSessionFilter.class.getSimpleName());
			filterDef.setFilter(new HibernateSessionFilter());
			ctx.addFilterDef(filterDef);

			FilterMap filterMap = new FilterMap();
			filterMap.setFilterName(HibernateSessionFilter.class.getSimpleName());
			filterMap.addURLPattern("/*");
			ctx.addFilterMap(filterMap);

			Wrapper wrapper = tomcat.addServlet(ctx, "JerseyServlet", new ServletContainer());
			wrapper.addInitParameter("javax.ws.rs.Application", "com.homeki.core.web.JerseyApplication");
			wrapper.addMapping("/*");
			wrapper.setLoadOnStartup(1);

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
