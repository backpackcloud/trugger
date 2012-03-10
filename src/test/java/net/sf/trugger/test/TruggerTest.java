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
package net.sf.trugger.test;

import junit.framework.AssertionFailedError;
import net.sf.trugger.Result;
import net.sf.trugger.element.Element;
import net.sf.trugger.predicate.Predicable;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.reflection.Access;
import net.sf.trugger.selector.AccessSelector;
import net.sf.trugger.selector.PredicateSelector;
import net.sf.trugger.test.bind.BinderTestSuite;
import net.sf.trugger.test.date.DateTestSuite;
import net.sf.trugger.test.element.ElementTestSuite;
import net.sf.trugger.test.factory.FactoryTest;
import net.sf.trugger.test.general.AcceptedTypesTest;
import net.sf.trugger.test.general.GeneralTestSuite;
import net.sf.trugger.test.general.UtilsTest;
import net.sf.trugger.test.interception.InterceptorTest;
import net.sf.trugger.test.iteration.IterationTest;
import net.sf.trugger.test.message.MessagesTest;
import net.sf.trugger.test.mock.AnnotationMockTest;
import net.sf.trugger.test.predicate.PredicatesTest;
import net.sf.trugger.test.predicate.ReflectionPredicatesTest;
import net.sf.trugger.test.property.PropertyTest;
import net.sf.trugger.test.reflection.ReflectionTestSuite;
import net.sf.trugger.test.registry.RegistryTest;
import net.sf.trugger.test.scan.ClassScanTestSuite;
import net.sf.trugger.test.transformer.TransformerTest;
import net.sf.trugger.test.validation.ValidationTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import java.lang.reflect.Member;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static net.sf.trugger.reflection.Access.DEFAULT;
import static net.sf.trugger.reflection.Access.LIKE_DEFAULT;
import static net.sf.trugger.reflection.Access.LIKE_PROTECTED;
import static net.sf.trugger.reflection.Access.PRIVATE;
import static net.sf.trugger.reflection.Access.PROTECTED;
import static net.sf.trugger.reflection.Access.PUBLIC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * This is the main test of the entire API. It includes all the separated tests
 * and has a common set of methods used by test classes.
 *
 * @author Marcelo Varella Barca Guimarães
 */
@RunWith(Suite.class)
@SuiteClasses( {
  UtilsTest.class,
  FactoryTest.class,
  MessagesTest.class,
  IterationTest.class,
  PropertyTest.class,
  RegistryTest.class,
  PredicatesTest.class,
  InterceptorTest.class,
  AcceptedTypesTest.class,
  AnnotationMockTest.class,
  TransformerTest.class,
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
   * @param collection
   *          the collection to test.
   * @param predicate
   *          the predicate to use.
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
   * @param element
   *          the element to test.
   * @param predicate
   *          the predicate to use.
   */
  public static <E> void assertMatch(E element, Predicate<? super E> predicate) {
    assertTrue(predicate.evaluate(element));
  }

  public static <E> void assertNotMatch(E element, Predicate<? super E> predicate) {
    assertFalse(predicate.evaluate(element));
  }

  /**
   * Tests a member access. The access must match with the given one.
   */
  public static <E> void assertAccess(Member member, Access access) {
    assertTrue(access.memberPredicate().evaluate(member));
  }

  /**
   * Tests a class access. The access must match with the given one.
   */
  public static <E> void assertAccess(Class<?> clazz, Access access) {
    assertTrue(access.classPredicate().evaluate(clazz));
  }

  /**
   * Tests the access of the given member. The access must <strong>not</strong>
   * match with the given one.
   */
  public static <E> void assertNotAccess(Member member, Access access) {
    assertFalse(access.memberPredicate().evaluate(member));
  }

  /**
   * Tests the access of the given class. The access must <strong>not</strong>
   * match with the given one.
   */
  public static <E> void assertNotAccess(Class<?> clazz, Access access) {
    assertFalse(access.classPredicate().evaluate(clazz));
  }

  /**
   * Tests if the given collection has only elements with the given names.
   */
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

  /**
   * Tests if the given command throws any of the specified exceptions.
   */
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

  /**
   * Tests if the given command does not throw any exception.
   */
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

  public static class SelectionAccessTest implements SelectionTest {

    private final SelectionTest test;
    private final Access access;

    public SelectionAccessTest(SelectionTest test, Access access) {
      this.test = test;
      this.access = access;
    }

    public void assertions(Object object) {
      test.assertions(object);
    }
    public Object createSelector() {
      return test.createSelector();
    }
    public void makeSelections(Object selector) {
      test.makeSelections(selector);
      ((AccessSelector) selector).withAccess(access);
    }
  };

  public static void assertAccessSelector(Access access, Object target, SelectionTest<AccessSelector, Object> test) {
    try {
      switch (access) {
        case PUBLIC:
          assertResult(new SelectionAccessTest(test, PUBLIC), target);
          assertNoResult(new SelectionAccessTest(test, PROTECTED), target);
          assertNoResult(new SelectionAccessTest(test, DEFAULT), target);
          assertNoResult(new SelectionAccessTest(test, PRIVATE), target);

          assertResult(new SelectionAccessTest(test, LIKE_DEFAULT), target);
          assertResult(new SelectionAccessTest(test, LIKE_PROTECTED), target);
          break;
        case PROTECTED:
          assertNoResult(new SelectionAccessTest(test, PUBLIC), target);
          assertResult(new SelectionAccessTest(test, PROTECTED), target);
          assertNoResult(new SelectionAccessTest(test, DEFAULT), target);
          assertNoResult(new SelectionAccessTest(test, PRIVATE), target);

          assertResult(new SelectionAccessTest(test, LIKE_DEFAULT), target);
          assertResult(new SelectionAccessTest(test, LIKE_PROTECTED), target);
          break;
        case DEFAULT:
          assertNoResult(new SelectionAccessTest(test, PUBLIC), target);
          assertNoResult(new SelectionAccessTest(test, PROTECTED), target);
          assertResult(new SelectionAccessTest(test, DEFAULT), target);
          assertNoResult(new SelectionAccessTest(test, PRIVATE), target);

          assertResult(new SelectionAccessTest(test, LIKE_DEFAULT), target);
          assertNoResult(new SelectionAccessTest(test, LIKE_PROTECTED), target);
          break;
        case PRIVATE:
          assertNoResult(new SelectionAccessTest(test, PUBLIC), target);
          assertNoResult(new SelectionAccessTest(test, PROTECTED), target);
          assertNoResult(new SelectionAccessTest(test, DEFAULT), target);
          assertResult(new SelectionAccessTest(test, PRIVATE), target);

          assertNoResult(new SelectionAccessTest(test, LIKE_DEFAULT), target);
          assertNoResult(new SelectionAccessTest(test, LIKE_PROTECTED), target);
          break;
      }
    } catch (Exception e) {
      throwError(e);
    }
  }

  public static void assertNoResult(Object result) {
    if(result instanceof Collection) {
      assertTrue(((Collection) result).isEmpty());
    } else {
      assertNull(result);
    }
  }

  public static void assertResult(Object result) {
    if(result instanceof Collection) {
      assertFalse(((Collection) result).isEmpty());
    } else {
      assertNotNull(result);
    }
  }

  public static void assertResult(Collection result, int count) {
    assertResult(result);
    assertEquals(count, result.size());
  }

  public static <T extends Result, E> void assertResult(SelectionTest<T,E> test, Object target) {
    T result = test.createSelector();
    test.makeSelections(result);
    Object obj = result.in(target);
    Predicate<E> predicate = ((Predicable) result).toPredicate();
    if(obj instanceof Collection) {
      assertFalse(((Collection) obj).isEmpty());
      assertMatch((Collection<E>) obj, predicate);
    } else {
      assertNotNull(obj);
      assertMatch((E) obj, predicate);
    }
    result = test.createSelector();
    ((PredicateSelector) result).thatMatches(predicate);
    Object obj2 = result.in(target);
    assertEquals(obj, obj2);
    test.assertions((E) obj);
    test.assertions((E) obj2);
  }

  public static <T extends Result, E extends Collection> void assertResult(final SelectionTest<T,E> test, Object target, final int size) {
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

  public static <T extends Result, E> void assertNoResult(SelectionTest<T,E> test, Object target) {
    T result = test.createSelector();
    test.makeSelections(result);
    Object obj = result.in(target);
    Predicate<E> predicate = ((Predicable) result).toPredicate();
    if(obj instanceof Collection) {
      assertTrue(((Collection) obj).isEmpty());
      assertNotMatch((Collection<E>) obj, predicate);
    } else {
      assertNull(obj);
    }
    result = test.createSelector();
    ((PredicateSelector) result).thatMatches(predicate);
    assertEquals(obj, result.in(target));
  }

}
