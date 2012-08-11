package com.homeki.core.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponseInterceptor;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.impl.NoConnectionReuseStrategy;
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

import com.homeki.core.http.handlers.DeviceHandler;
import com.homeki.core.http.handlers.DeviceMockHandler;
import com.homeki.core.http.handlers.DeviceStateHandler;
import com.homeki.core.http.handlers.DeviceTellstickHandler;
import com.homeki.core.http.handlers.ServerHandler;
import com.homeki.core.http.handlers.TriggerConditionHandler;
import com.homeki.core.http.handlers.TriggerHandler;
import com.homeki.core.logging.L;
import com.homeki.core.main.Configuration;
import com.homeki.core.main.ControlledThread;

public class HttpListenerThread extends ControlledThread {
	private HttpParams params;
	private HttpService service;
	private ServerSocket listenSocket;
	private ExecutorService pool;
	
	public HttpListenerThread() throws IOException {
		super(0);
		this.listenSocket = new ServerSocket(5001, 10, null);
		
		this.params = new SyncBasicHttpParams();
		this.params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 2500);
		this.params.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 1024);
		this.params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
		this.params.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true);
		this.params.setParameter(CoreProtocolPNames.ORIGIN_SERVER, "HttpComponents/1.1");
		
		HttpRequestHandlerRegistry registry = new HttpRequestHandlerRegistry();
		registry.register("/device/state/*", new DeviceStateHandler());
		registry.register("/device/tellstick/*", new DeviceTellstickHandler());
		registry.register("/device/mock/*", new DeviceMockHandler());
		registry.register("/device/*", new DeviceHandler());
		registry.register("/server/*", new ServerHandler());
		//registry.register("/trigger/action/*", new TriggerActionHandler());
		//registry.register("/trigger/actiongroup/*", new TriggerActionGroupHandler());
		registry.register("/trigger/*", new TriggerHandler());
		registry.register("/trigger/condition/*", new TriggerConditionHandler());
		//registry.register("/actiongroup/action/*", new ActionGroupActionHandler());
		//registry.register("/actiongroup/actiongroup/*", new ActionGroupActionGroupHandler());
		//registry.register("/actiongroup/*", new ActionGroupHandler());
		
		HttpProcessor proc = new ImmutableHttpProcessor(new HttpResponseInterceptor[] { new ResponseDate(), new ResponseServer(), new ResponseContent(), new ResponseConnControl() });
		
		this.service = new HttpService(proc, new NoConnectionReuseStrategy(), new DefaultHttpResponseFactory(), registry, this.params);
		
		pool = Executors.newFixedThreadPool(Configuration.HTTP_THREAD_POOL_SIZE);
	}
	
	@Override
	public void iteration() throws InterruptedException {
		try {
			Socket socket = listenSocket.accept();
			DefaultHttpServerConnection conn = new DefaultHttpServerConnection();
			conn.bind(socket, params);
			
			pool.submit(new HttpRequestResolverRunnable(service, conn));
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
		} catch (IOException ignore) {}
	}
}