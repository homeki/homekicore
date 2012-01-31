package com.homeki.core.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.apache.http.HttpResponseInterceptor;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

import com.homeki.core.http.handlers.HttpDeviceHandler;
import com.homeki.core.http.handlers.HttpGetHandler;
import com.homeki.core.http.handlers.HttpSetHandler;
import com.homeki.core.http.handlers.HttpStateHandler;
import com.homeki.core.http.handlers.HttpTimerTriggerHandler;
import com.homeki.core.http.handlers.HttpTriggerHandler;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.L;

public class HttpListenerThread extends ControlledThread {
	private HttpParams params;
	private HttpService service;
	private ServerSocket listenSocket;
	
	public HttpListenerThread(HttpApi api) throws IOException {
		super(0);
		this.listenSocket = new ServerSocket(5000, 10, null);
		
        this.params = new SyncBasicHttpParams();
        this.params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 2500);
        this.params.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 1024);
        this.params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
        this.params.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true);
        this.params.setParameter(CoreProtocolPNames.ORIGIN_SERVER, "HttpComponents/1.1");
        
        HttpRequestHandlerRegistry registry = new HttpRequestHandlerRegistry();
        registry.register("/get/*", new HttpGetHandler(api));
        registry.register("/set/*", new HttpSetHandler(api));
        registry.register("/trigger/*", new HttpTriggerHandler(api));
        registry.register("/trigger/timer/*", new HttpTimerTriggerHandler(api));
        registry.register("/device/*", new HttpDeviceHandler(api));
        registry.register("/state/*", new HttpStateHandler(api));
        
        HttpProcessor proc = new ImmutableHttpProcessor(new HttpResponseInterceptor[] {
                new ResponseDate(),
                new ResponseServer(),
                new ResponseContent(),
                new ResponseConnControl()
        });
        
        this.service = new HttpService(proc, new DefaultConnectionReuseStrategy(), new DefaultHttpResponseFactory(), registry, this.params);
	}
	
	@Override
	public void iteration() throws InterruptedException {
		try {
			Socket socket = listenSocket.accept();
            DefaultHttpServerConnection conn = new DefaultHttpServerConnection();
            conn.bind(socket, this.params);

            Thread t = new HttpRequestResolverThread(this.service, conn);
            t.start();
		} catch (SocketException e) {
			L.i("Closed listener socket.");
		} catch (IOException e) {
			L.e("Unknown exception when receiving new HTTP request.", e);
		}
	}
	
	@Override
	public void shutdown() {
		super.shutdown();
		try {
			listenSocket.close();
		} catch (IOException e) {}
	}
}