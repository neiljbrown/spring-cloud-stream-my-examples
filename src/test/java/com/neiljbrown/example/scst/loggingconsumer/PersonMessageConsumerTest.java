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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link PersonMessageConsumer}.
 */
public class PersonMessageConsumerTest {

  /** Instance of class under test. */
  private PersonMessageConsumer messageConsumer;

  /**
   * Performs the initialisation (setup) required before executing every test.
   */
  @BeforeEach
  void setUp() {
    this.messageConsumer = new PersonMessageConsumer();
  }

  /**
   * Tests message handler method {@link PersonMessageConsumer#handle(Person)}.
   */
  @Test
  void test_handle() {
    this.messageConsumer.handle(new Person("Joe", "Bloggs"));
  }
}
