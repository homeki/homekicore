package com.homeki.core.http;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Status;

public class RestletServerResource extends Restlet {
	@Override
	public void handle(Request request, Response response) {
		response.setEntity("test", MediaType.TEXT_PLAIN);
		response.setStatus(Status.SUCCESS_OK);
	}
}
