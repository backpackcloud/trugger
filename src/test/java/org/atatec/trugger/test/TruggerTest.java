/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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
package org.atatec.trugger.test;

import junit.framework.AssertionFailedError;
import org.atatec.trugger.Result;
import org.atatec.trugger.element.Element;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.Assert.*;

/**
 * This is the main test of the entire API. It includes all the separated tests and has a
 * common set of methods used by test classes.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerTest {

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
   * Tests if the given collection has only elements with the given names.
   */
  public static void assertElements(Collection<? extends Element> collection,
                                    String... names) {
    Set<String> elNames = new HashSet<String>(collection.size());
    for (Element el : collection) {
      elNames.add(el.name());
    }
    assertEquals(names.length, elNames.size());
    for (String name : names) {
      assertTrue(elNames.contains(name));
    }
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

  /**
   * Tests if the given command does not throw anyThat exception.
   */
  public static void assertNothingThrow(Runnable command) {
    try {
      command.run();
    } catch (Throwable e) {
      throwError(e);
    }
  }

  private static void throwError(Throwable e) throws AssertionFailedError {
    AssertionFailedError error = new AssertionFailedError("Exception "
        + e.getClass() + " thrown.");
    error.initCause(e);
    throw error;
  }

  public static void assertNoResult(Object result) {
    if (result instanceof Collection) {
      assertTrue(((Collection) result).isEmpty());
    } else {
      assertNull(result);
    }
  }

  public static void assertResult(Object result) {
    if (result instanceof Collection) {
      assertFalse(((Collection) result).isEmpty());
    } else {
      assertNotNull(result);
    }
  }

  public static void assertResult(Collection result, int count) {
    assertResult(result);
    assertEquals(count, result.size());
  }

  public static <T extends Result, E> void assertResult(SelectionTest<T, E> test, Object target) {
    T result = test.createSelector();
    test.makeSelections(result);
    Object obj = result.in(target);
    if (obj instanceof Collection) {
      assertFalse(((Collection) obj).isEmpty());
    } else {
      assertNotNull(obj);
    }
  }

  public static <T extends Result, E extends Collection> void assertResult(final SelectionTest<T, E> test, Object target, final int size) {
    assertResult(new SelectionTest<T, E>() {
      public T createSelector() {
        return test.createSelector();
      }

      public void makeSelections(T selector) {
        test.makeSelections(selector);
      }

      public void assertions(E col) {
        assertEquals(size, col.size());
      }
    }, target);
  }

  public static <T extends Result, E> void assertNoResult(SelectionTest<T, E> test, Object target) {
    T result = test.createSelector();
    test.makeSelections(result);
    Object obj = result.in(target);
    if (obj instanceof Collection) {
      assertTrue(((Collection) obj).isEmpty());
    } else {
      assertNull(obj);
    }
  }

  public static ElementMock element() {
    return new ElementMock();
  }

  public static ElementFinderMock elementFinder() {
    return new ElementFinderMock();
  }

}
