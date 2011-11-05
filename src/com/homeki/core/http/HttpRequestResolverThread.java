package com.homeki.core.http;

import java.io.IOException;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpService;

import com.homeki.core.log.L;
import com.homeki.core.threads.ControlledThread;

public class HttpRequestResolverThread extends ControlledThread {
	private HttpService httpservice;
    private HttpServerConnection conn;
    private HttpContext context;
	
    public HttpRequestResolverThread(HttpService httpservice, HttpServerConnection conn) {
		super(1000);
		this.context = new BasicHttpContext(null);
        this.httpservice = httpservice;
        this.conn = conn;
	}
    
    public void run() {

    }

	@Override
	public void iteration() throws InterruptedException {
        try {
            while (!Thread.interrupted() && this.conn.isOpen()) {
                this.httpservice.handleRequest(this.conn, context);
            }
        } catch (ConnectionClosedException ex) {
            L.e("Client closed connection.", ex);
        } catch (IOException ex) {
            L.e("I/O error.", ex);
        } catch (HttpException ex) {
            L.e("Unrecoverable HTTP protocol violation.", ex);
        } finally {
            try {
                this.conn.shutdown();
            } catch (IOException ignore) {}
        }
	}
}
