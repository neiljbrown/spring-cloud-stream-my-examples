/*
 * *********************************************************************************************************************
 * Copyright BrightTALK Ltd, 2019 - present.
 * All Rights Reserved.
 * *********************************************************************************************************************
 */

package com.neiljbrown.example.scst.loggingconsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.core.JsonParseException;

/**
 * A narrow set of integration tests that launch the Spring container to test the integration of the
 * {@link PersonMessageConsumer} (bean) with its supporting event-driven messaging framework Spring Cloud Stream (SCSt).
 *
 * <h2>Test Implementation</h2>
 * This is an 'in-process' test suite - the tests run in the same process (JVM for the JUnit test runner) as the code
 * under test.
 * <p>
 * For speed and simplicity, the tests do _not_ rely on (launching or) connecting to a running instance of the
 * service's message broker. They stub the broker instead. This is currently implemented using SCS' testing support,
 * specifically its TestSupportBinder. The TestSupportBinder is declared on the test runtime classpath where it is
 * detected by Spring Boot's auto (bean) configuration, which results in the test binder superseding the binder for the
 * environment's real message binder. (For more details see the 'Testing' chapter of the SCS Reference manual).
 *
 * <h2>Included Test Coverage/Scope</h2>
 * This Spring container integration test provides the following additional test coverage, over and above pure unit
 * tests of the message processor -
 * <ul>
 * <li>That the required SCS binder (broker) and binding (channel) application config is present and correct.
 * (This may already be satisfied by any other Spring integration test that load the Spring container, e.g.
 * {@link ApplicationSpringIntegrationTests}).</li>
 * <li>That the SCS {@link StreamListener} annotation, used to declare message handler methods in the class under
 * test, has been applied correctly and hence working as expected, including e.g.
 * <br>
 * - Testing that the methods are using the correct binding, i.e. they're bound to and listening on the expected input
 * message channel;
 * <br>
 * - Testing that SCS' "conditional dispatching" mechanism is working as expected. The handler method should only
 * receive events of the expected type of message based on using @StreamListener's 'condition' attribute to match the
 * ‘event-type’ message header.
 * </li>
 * <li>That  messages that SCS dispatches can be de-serialised and converted to each message handler method's specified
 * class/type of parameter(s).</li>
 * </ul>
 *
 * <h2>Excluded Test Coverage/Scope</h2>
 * In the interest of speed and simplicity these tests stub the service's message broker by using a test SCS binder.
 * As such they do not extend to testing integration with either a real SCS binder or message broker  (e.g. the one
 * used in production), or any behaviour that is unique to the particular implementation of them. Such additional test
 * coverage is provided by other, higher level types of integration or component tests.
 *
 * @see PersonMessageConsumer
 * @see PersonMessageConsumerTest
 */
// Bootstrap app using Spring Boot support e.g. load application.properties, create default beans based on classpath,
// enable execution of Spring TestContext support in JUnit test case lifecycle, etc
@SpringBootTest(classes = Application.class)
public class PersonMessageConsumerSpringIntegrationTest {

  private final Sink inputMessageChannelBinding;

  /**
   * @param inputMessageChannelBinding the SCS bindings interface declaring the (input) message channel to which the
   * message consumer under test is expected to be bound and consume messages from.
   */
  @Autowired
  public PersonMessageConsumerSpringIntegrationTest(Sink inputMessageChannelBinding) {
    this.inputMessageChannelBinding = inputMessageChannelBinding;
  }

  /**
   * Tests {@link PersonMessageConsumer#handle(Person)} when a valid message is received in the input message channel
   * on which the class under test is expected to be bound/listening.
   */
  @Test
  void test_handle_whenValidPersonMessageReceived() {
    String personAsJsonString = "{" +
      "\"firstName\":\"Joe\"," +
      "\"lastName\":\"Bloggs\"" +
    "}";
    Map<String, Object> headers = Map.of("event-type", "PersonCreated");
    Message<String> personMessage = this.createMessageWithStringPayload(headers, personAsJsonString);

    // Send the event message to the message channel on which the message processor should be listening.
    this.inputMessageChannelBinding.input().send(personMessage);

    // Method under test doesn't currently have any side effects / generate any outputs so there is nothing to assert
  }

  /**
   * Tests {@link PersonMessageConsumer#handle(Person) when the message received in the input message channel
   * on which the class under test is expected to be bound/listening contains an invalid payload - one that cannot be
   * deserialised to a Person object, because the serialised representation is malformed (invalid JSON).
   * <p>
   * The test serves to establish how such an error scenario (invalid message) is handled by the  messaging framework
   * by default.
   */
  @Test
  void test_handle_whenMalformedMessagePayloadReceived()  {
    Map<String, Object> headers = Map.of("event-type", "PersonUpdated");
    String payload = "{invalid-malformed-json}";
    Message<String> message = this.createMessageWithStringPayload(headers, payload);

    // Send the event message to the message channel on which the message processor should be listening.
    // Sending a malformed message results in an exception being thrown when the messaging framework attempts to
    // deserialise the body of the message as part of dispatching it to the message handler method.
    Throwable throwable = catchThrowable(() ->
      this.inputMessageChannelBinding.input().send(message)
    );

    assertThat(throwable)
      .isInstanceOf(MessageConversionException.class)
      .hasCauseInstanceOf(JsonParseException.class);
  }

  /**
   * Creates and returns a {@link Message message} with a payload set to a supplied string, and an optional set of
   * headers.
   *
   * @param headers the set of headers to include in the message. Optional (can be null).
   * @param payload the String that should be used as the payload of the message.
   * @return the created {@link Message}.
   */
  private Message<String> createMessageWithStringPayload(Map<String, Object> headers, String payload) {
    return new GenericMessage<>(payload, new MessageHeaders(headers));
  }
}