package de.turing85.camel.errorhandler;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.direct;

@Slf4j
public class MyDirectRoute extends RouteBuilder {
  public static final String DIRECT_ROUTE = "direct-route";

  @Override
  public void configure() {
    // @formatter:off
    onException(Exception.class)
        .maximumRedeliveries(3)
        .log("Exception caught ${exchangeProperty.%s}".formatted(Exchange.EXCEPTION_CAUGHT))
        .onRedelivery(exchange -> log.info(
            "Redelivery {} / {}",
            exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER),
            exchange.getIn().getHeader(Exchange.REDELIVERY_MAX_COUNTER)));

    from(direct(DIRECT_ROUTE))
        .id(DIRECT_ROUTE)
        .log("Hello from direct route");
    // @formatter:on
  }
}
