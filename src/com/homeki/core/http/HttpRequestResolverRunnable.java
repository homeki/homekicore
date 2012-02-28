package com.homeki.core.http;

import java.io.IOException;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpService;

import com.homeki.core.main.L;

public class HttpRequestResolverRunnable implements Runnable {
	private HttpService httpservice;
    private HttpServerConnection conn;
    private HttpContext context;
	
    public HttpRequestResolverRunnable(HttpService httpservice, HttpServerConnection conn) {
		this.context = new BasicHttpContext(null);
        this.httpservice = httpservice;
        this.conn = conn;
	} 	

	@Override
	public void run() {
		Thread.currentThread().setName("HttpRequestResolverWorker");
        try {
            while (!Thread.interrupted() && this.conn.isOpen()) {
                this.httpservice.handleRequest(this.conn, context);
            }
        } catch (ConnectionClosedException ignore) {
        } catch (IOException ignore) {
        } catch (HttpException e) {
            L.e("Unrecoverable HTTP protocol violation.", e);
        } finally {
            try {
                this.conn.shutdown();
            } catch (IOException ignore) {}
        }
	}
}
