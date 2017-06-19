/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.devnull.trugger;

import junit.framework.AssertionFailedError;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This is the main test of the entire API. It includes all the separated tests and has a
 * common set of methods used by test classes.
 *
 * @author Marcelo "Ataxexe" Guimarães
 */
public class TruggerTest {

  public static final Function<Collection, Integer> SIZE = collection -> collection.size();

  public static Function<List, ?> valueAt(int index) {
    return list -> list.get(index);
  }

  /**
   * Tests the collection elements with the given predicate.
   *
   * @param collection the collection to test.
   * @param predicate  the predicate to use.
   */
  public static <E> void assertMatch(Collection<? extends E> collection,
                                     Predicate<? super E> predicate) {
    for (E element : collection) {
      assertMatch(element, predicate);
    }
  }

  public static <E> void assertNotMatch(Collection<? extends E> collection,
                                        Predicate<? super E> predicate) {
    for (E element : collection) {
      assertNotMatch(element, predicate);
    }
  }

  /**
   * Tests the element with the given predicate.
   *
   * @param element   the element to test.
   * @param predicate the predicate to use.
   */
  public static <E> void assertMatch(E element,
                                     Predicate<? super E> predicate) {
    assertTrue(predicate.test(element));
  }

  public static <E> void assertNotMatch(E element,
                                        Predicate<? super E> predicate) {
    assertFalse(predicate.test(element));
  }

  /**
   * Tests if the given command throws anyThat of the specified exceptions.
   */
  public static void assertThrow(Class<? extends Throwable> exception,
                                 Runnable command) {
    try {
      command.run();
      throw new AssertionFailedError("No exception thrown.");
    } catch (Throwable e) {
      if (exception.isAssignableFrom(e.getClass())) {
        return;
      }
      throwError(e);
    }
  }

  public static <E> void assertThrow(Class<? extends Throwable> exception,
                                     E object,
                                     Consumer<E> consumer) {
    try {
      consumer.accept(object);
      throw new AssertionFailedError("No exception thrown.");
    } catch (Throwable e) {
      if (exception.isAssignableFrom(e.getClass())) {
        return;
      }
      throwError(e);
    }
  }

  private static void throwError(Throwable e) throws AssertionFailedError {
    AssertionFailedError error = new AssertionFailedError("Exception "
        + e.getClass() + " thrown.");
    error.initCause(e);
    throw error;
  }

  public static ElementMock element() {
    return new ElementMock();
  }

}
