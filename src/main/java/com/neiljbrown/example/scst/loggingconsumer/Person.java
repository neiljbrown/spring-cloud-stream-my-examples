/*
 *
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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A Person in this domain.
 */
public class Person {
  private String firstName;
  private String lastName;

  @JsonCreator
  Person(@JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Person{");
    sb.append("firstName='").append(this.firstName).append("'");
    sb.append(", lastName='").append(this.lastName).append("'");
    sb.append("}");
    return sb.toString();
  }
}