/*
 * Copyright 2009-2014 Marcelo Guimarães
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.devnull.trugger.interception;

import org.junit.Test;
import tools.devnull.trugger.validation.validator.NotNull;
import tools.devnull.trugger.validation.validator.Valid;

import java.util.function.Function;

import static org.junit.Assert.*;
import static tools.devnull.trugger.TruggerTest.assertThrow;

/**
 *
 */
public class ValidationInterceptionTest {

  public class MyObject {

    @NotNull
    String string;

  }

  public static class MyFunction implements Function<MyObject, Object> {

    @Override
    public Object apply(@NotNull @Valid MyObject value) {
      return value;
    }
  }

  @Test
  public void testValidHandler() {
    Function function = Interception.intercept(Function.class)
        .on(new MyFunction())
        .onCall(new ValidationInterceptionHandler()
            .onValid((context) -> null))
        .proxy();
    MyObject object = new MyObject();
    object.string = "";
    assertNull(function.apply(object));
  }

  @Test
  public void testInvalidHandler() {
    Function function = Interception.intercept(Function.class)
        .on(new MyFunction())
        .onCall(new ValidationInterceptionHandler())
        .proxy();
    assertThrow(IllegalArgumentException.class,
        () -> function.apply(new MyObject()));

    assertThrow(IllegalArgumentException.class,
        () -> function.apply(null));
  }

  public static interface MyInterface {

    boolean doIt(@NotNull Object object);

  }

  @Test
  public void testInterfaceAnnotations() {
    MyInterface test = Interception.intercept(MyInterface.class)
        .onCall(new ValidationInterceptionHandler()
            .onValid(context -> true)
            .onInvalid(context -> false))
        .proxy();
    assertTrue(test.doIt(new Object()));
    assertFalse(test.doIt(null));
  }

}
