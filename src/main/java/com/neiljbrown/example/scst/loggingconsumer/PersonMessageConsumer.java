/*
 * Copyright 2019-present the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.neiljbrown.example.scst.loggingconsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

/**
 * An example of a POJO-based message consumer implemented using the Spring Cloud Stream framework.
 */
@Component
class PersonMessageConsumer {

  private static final Logger logger = LoggerFactory.getLogger(PersonMessageConsumer.class);

  /**
   * Handler method for processing messages received by an input message channel bound to {@link Sink#INPUT}.
   * <p>
   * Uses Spring Cloud Stream's (SCS) {@link StreamListener} annotation to declare this method as a handler for messages
   * received in the bound input message channel. SCS takes care of extracting the message payload, and deserialising,
   * converting and binding it to an instance of the declared method argument type -  in this case a  {@link Person}.
   *
   * @param person the deserialised representation of the {@link Person} contained in the received message.
   */
  @StreamListener(Sink.INPUT)
  void handle(Person person) {
    logger.info("Received person [" + person + "].");
  }
}