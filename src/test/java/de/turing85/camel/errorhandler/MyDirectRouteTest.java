package de.turing85.camel.errorhandler;

import com.google.common.truth.Truth;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.test.main.junit5.CamelMainTestSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MyDirectRouteTest extends CamelMainTestSupport {
  public static final String DIRECT_ROUTE_URI = "direct:%s".formatted(MyDirectRoute.DIRECT_ROUTE);

  @Override
  protected Class<?> getMainClass() {
    return Application.class;
  }

  @Override
  public boolean isUseAdviceWith() {
    return true;
  }

  @Test
  void triggerRoute() throws Exception {
    // Given
    final Exception expectedException = new Exception("Test exception behaviour");
    // @formatter:off
    AdviceWith.adviceWith(
        context,
        MyDirectRoute.DIRECT_ROUTE,
        advice -> advice.weaveAddLast().throwException(expectedException));
    context.start();

    // When
    final Throwable actualException;
    try (ProducerTemplate template = context.createProducerTemplate())  {
      final CamelExecutionException camelExecutionException = Assertions.assertThrows(
          CamelExecutionException.class,
          () -> template.sendBody(DIRECT_ROUTE_URI, ""));
      actualException = camelExecutionException.getCause();
    }
    // @formatter:on

    // Then
    Truth.assertThat(actualException).isSameInstanceAs(expectedException);
  }
}
