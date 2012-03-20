package com.homeki.core.http;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.hibernate.Session;

public class Container {
	public HttpRequest req;
	public HttpResponse res;
	public Session session;
	public List<NameValuePair> qs;
	public StringTokenizer path;
}
