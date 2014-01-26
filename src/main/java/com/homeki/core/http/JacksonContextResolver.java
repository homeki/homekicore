package com.homeki.core.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import java.text.SimpleDateFormat;

@Produces(MediaType.WILDCARD)
public class JacksonContextResolver implements ContextResolver<ObjectMapper> {
  private static final Logger LOG = LoggerFactory.getLogger(JacksonContextResolver.class);

  private final ObjectMapper objectMapper;

  public JacksonContextResolver() {
    objectMapper = new ObjectMapper();
    configure();
  }

  private void configure() {
    LOG.info("Configuring custom ObjectMapper.");
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @Override
  public ObjectMapper getContext(Class<?> type) {
    return objectMapper;
  }
}
