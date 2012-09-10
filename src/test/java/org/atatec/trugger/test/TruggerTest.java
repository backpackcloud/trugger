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
import org.atatec.trugger.predicate.Predicable;
import org.atatec.trugger.predicate.Predicate;
import org.atatec.trugger.selector.PredicateSelector;
import org.atatec.trugger.test.bind.BinderTestSuite;
import org.atatec.trugger.test.date.DateTestSuite;
import org.atatec.trugger.test.element.ElementTestSuite;
import org.atatec.trugger.test.exception.ExceptionHandlingTest;
import org.atatec.trugger.test.factory.FactoryTest;
import org.atatec.trugger.test.general.AcceptedTypesTest;
import org.atatec.trugger.test.general.GeneralTestSuite;
import org.atatec.trugger.test.general.UtilsTest;
import org.atatec.trugger.test.interception.InterceptorTest;
import org.atatec.trugger.test.iteration.IterationTest;
import org.atatec.trugger.test.message.MessagesTest;
import org.atatec.trugger.test.mock.AnnotationMockTest;
import org.atatec.trugger.test.predicate.PredicatesTest;
import org.atatec.trugger.test.predicate.ReflectionPredicatesTest;
import org.atatec.trugger.test.property.PropertyTest;
import org.atatec.trugger.test.reflection.ReflectionTestSuite;
import org.atatec.trugger.test.registry.RegistryTest;
import org.atatec.trugger.test.scan.ClassScanTestSuite;
import org.atatec.trugger.test.transformer.TransformerTest;
import org.atatec.trugger.test.validation.ValidationTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * This is the main test of the entire API. It includes all the separated tests and has a
 * common set of methods used by test classes.
 *
 * @author Marcelo Varella Barca Guimarães
 */
@RunWith(Suite.class)
@SuiteClasses({
  UtilsTest.class,
  FactoryTest.class,
  MessagesTest.class,
  IterationTest.class,
  PropertyTest.class,
  RegistryTest.class,
  PredicatesTest.class,
  InterceptorTest.class,
  TransformerTest.class,
  AcceptedTypesTest.class,
  AnnotationMockTest.class,
  ExceptionHandlingTest.class,
  ReflectionPredicatesTest.class,

  DateTestSuite.class,
  BinderTestSuite.class,
  GeneralTestSuite.class,
  ElementTestSuite.class,
  ClassScanTestSuite.class,
  ReflectionTestSuite.class,
  ValidationTestSuite.class
})
public class TruggerTest {

  /**
   * Tests the collection elements with the given predicate.
   *
   * @param collection the collection to test.
   * @param predicate  the predicate to use.
   */
  public static <E> void assertMatch(Collection<? extends E> collection, Predicate<? super E> predicate) {
    for (E element : collection) {
      assertMatch(element, predicate);
    }
  }

  public static <E> void assertNotMatch(Collection<? extends E> collection, Predicate<? super E> predicate) {
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
  public static <E> void assertMatch(E element, Predicate<? super E> predicate) {
    assertTrue(predicate.evaluate(element));
  }

  public static <E> void assertNotMatch(E element, Predicate<? super E> predicate) {
    assertFalse(predicate.evaluate(element));
  }

  /** Tests if the given collection has only elements with the given names. */
  public static void assertElements(Collection<? extends Element> collection, String... names) {
    Set<String> elNames = new HashSet<String>(collection.size());
    for (Element el : collection) {
      elNames.add(el.name());
    }
    assertEquals(names.length, elNames.size());
    for (String name : names) {
      assertTrue(elNames.contains(name));
    }
  }

  /** Tests if the given command throws any of the specified exceptions. */
  public static void assertThrow(Runnable command, Class<? extends Throwable>... exceptions) {
    try {
      command.run();
      throw new AssertionFailedError("No exception thrown.");
    } catch (Throwable e) {
      for (Class<? extends Throwable> exception : exceptions) {
        if (exception.isAssignableFrom(e.getClass())) {
          return;
        }
      }
      throwError(e);
    }
  }

  /** Tests if the given command does not throw any exception. */
  public static void assertNothingThrow(Runnable command) {
    try {
      command.run();
    } catch (Throwable e) {
      throwError(e);
    }
  }

  private static void throwError(Throwable e) throws AssertionFailedError {
    AssertionFailedError error = new AssertionFailedError("Exception " + e.getClass() + " thrown.");
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
    Predicate<E> predicate = ((Predicable) result).toPredicate();
    if (obj instanceof Collection) {
      assertFalse(((Collection) obj).isEmpty());
      assertMatch((Collection<E>) obj, predicate);
    } else {
      assertNotNull(obj);
      assertMatch((E) obj, predicate);
    }
    result = test.createSelector();
    ((PredicateSelector) result).that(predicate);
    Object obj2 = result.in(target);
    assertEquals(obj, obj2);
    test.assertions((E) obj);
    test.assertions((E) obj2);
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
    Predicate<E> predicate = ((Predicable) result).toPredicate();
    if (obj instanceof Collection) {
      assertTrue(((Collection) obj).isEmpty());
      assertNotMatch((Collection<E>) obj, predicate);
    } else {
      assertNull(obj);
    }
    result = test.createSelector();
    ((PredicateSelector) result).that(predicate);
    assertEquals(obj, result.in(target));
  }

}
