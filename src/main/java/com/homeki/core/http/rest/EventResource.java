package com.homeki.core.http.rest;

import com.homeki.core.events.*;
import com.homeki.core.http.ApiException;
import com.homeki.core.json.JsonSpecialValue;
import com.homeki.core.logging.L;
import com.homeki.core.main.Util;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/event")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EventResource {
	public static class EventStreamWriter implements EventListener {
		private final EventOutput output;

		public EventStreamWriter(EventOutput output) {
			this.output = output;
		}

		@Override
		public void onEvent(Event event) {
			if (!(event instanceof ChannelValueChangedEvent)) return;

			ChannelValueChangedEvent cvce = (ChannelValueChangedEvent)event;

			OutboundEvent.Builder oeb = new OutboundEvent.Builder();
			oeb.data(cvce);
			oeb.mediaType(MediaType.APPLICATION_JSON_TYPE);
			OutboundEvent oe = oeb.build();

			try {
				output.write(oe);
			} catch (IOException e) {
				L.i("Failed to write event, unsubscribing.");
				EventQueue.INSTANCE.unsubscribe(this);
				try {
					output.close();
				} catch (IOException ignore) { }
			}
		}
	}

	@GET
	@Path("/stream")
	@Produces(SseFeature.SERVER_SENT_EVENTS)
	public EventOutput eventStream() {
		EventOutput output = new EventOutput();
		EventQueue.INSTANCE.subscribe(new EventStreamWriter(output));
		return output;
	}

	@POST
	@Path("/specialvalue")
	public Response triggerSpecialValue(JsonSpecialValue jvalue) {
		if (Util.nullOrEmpty(jvalue.source))
			throw new ApiException("Source cannot be empty.");
		if (jvalue.value == null)
			throw new ApiException("Value cannot be empty.");

		EventQueue.INSTANCE.add(SpecialValueChangedEvent.createCustomEvent(jvalue.source, jvalue.value));

		return Response.ok("Special value changed event triggered successfully.").build();
	}
}
