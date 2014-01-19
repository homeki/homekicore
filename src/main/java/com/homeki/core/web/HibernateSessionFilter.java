package com.homeki.core.web;

import com.homeki.core.storage.Hibernate;
import org.hibernate.Session;

import javax.servlet.*;
import java.io.IOException;

public class HibernateSessionFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		Session session = Hibernate.openSession();

		try {
			chain.doFilter(request, response);
		} finally {
			Hibernate.closeSession(session);
		}
	}

	@Override
	public void destroy() {

	}
}
