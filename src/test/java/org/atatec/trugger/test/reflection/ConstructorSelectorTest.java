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
package org.atatec.trugger.test.reflection;

import static org.atatec.trugger.reflection.Reflection.reflect;
import static org.atatec.trugger.test.TruggerTest.assertResult;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.atatec.trugger.predicate.Predicates;
import org.atatec.trugger.reflection.Access;
import org.atatec.trugger.reflection.ReflectionException;
import org.atatec.trugger.selector.ConstructorSelector;
import org.atatec.trugger.test.Flag;
import org.atatec.trugger.test.SelectionTest;
import org.atatec.trugger.test.SelectionTestAdapter;
import org.atatec.trugger.test.TruggerTest.SelectionAccessTest;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ConstructorSelectorTest {

	static class AnnotatedSelectorTest {
		@Flag
		AnnotatedSelectorTest() {}
		AnnotatedSelectorTest(int i) {}
	}

	@Test
  public void testNoSelector() {
    assertResult(new SelectionTestAdapter(){
      public Object createSelector() {
        return reflect().constructor();
      }
    }, Object.class);
  }

	@Test(expected = ReflectionException.class)
	public void testInsufficientSelector() {
	  reflect().constructor().in(AnnotatedSelectorTest.class);
	}

	@Test
	public void testAnnoatedSelector() {
	  SelectionTest<ConstructorSelector, Constructor> selection = new SelectionTest<ConstructorSelector, Constructor>() {
      public void makeSelections(ConstructorSelector selector) {
        selector.annotated();
      }
      public ConstructorSelector createSelector() {
        return reflect().constructor();
      }
      public void assertions(Constructor constructor) {
        assertTrue(constructor.isAnnotationPresent(Flag.class));
      }
    };
    assertResult(selection, AnnotatedSelectorTest.class);
	}

	@Test
	public void testNotAnnoatedSelector() {
	  SelectionTest<ConstructorSelector, Constructor> selection = new SelectionTest<ConstructorSelector, Constructor>() {
      public void makeSelections(ConstructorSelector selector) {
        selector.notAnnotated();
      }
      public ConstructorSelector createSelector() {
        return reflect().constructor();
      }
      public void assertions(Constructor constructor) {
        assertFalse(constructor.isAnnotationPresent(Flag.class));
      }
    };
    assertResult(selection, AnnotatedSelectorTest.class);
	}

	@Test
  public void testAnnoatedWithSelector() {
	  SelectionTest<ConstructorSelector, Constructor> selection = new SelectionTest<ConstructorSelector, Constructor>() {
      public void makeSelections(ConstructorSelector selector) {
        selector.annotatedWith(Flag.class);
      }
      public ConstructorSelector createSelector() {
        return reflect().constructor();
      }
      public void assertions(Constructor constructor) {
        assertTrue(constructor.isAnnotationPresent(Flag.class));
      }
    };
    assertResult(selection, AnnotatedSelectorTest.class);
  }

  @Test
  public void testNotAnnoatedWithSelector() {
    SelectionTest<ConstructorSelector, Constructor> selection = new SelectionTest<ConstructorSelector, Constructor>() {
      public void makeSelections(ConstructorSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
      public ConstructorSelector createSelector() {
        return reflect().constructor();
      }
      public void assertions(Constructor constructor) {
        assertFalse(constructor.isAnnotationPresent(Flag.class));
      }
    };
    assertResult(selection, AnnotatedSelectorTest.class);
  }

  @Test
  public void testPredicateSelector() {
    assertNotNull(reflect().constructor().that(Predicates.ALWAYS_TRUE).withoutParameters().in(Object.class));
    assertNull(reflect().constructor().that(Predicates.ALWAYS_FALSE).withoutParameters().in(Object.class));
  }

  static class AccessSelectorTest {
    public AccessSelectorTest() {}
    private AccessSelectorTest(int i) {}
    AccessSelectorTest(String s) {}
    protected AccessSelectorTest(double d) {}
  }

  @Test
  public void testAccessSelector() {
    SelectionTest test = new SelectionTestAdapter() {
      public Object createSelector() {
        return reflect().constructors();
      }
    };
    assertResult(new SelectionAccessTest(test, Access.PUBLIC), AccessSelectorTest.class, 1);
    assertResult(new SelectionAccessTest(test, Access.PROTECTED), AccessSelectorTest.class, 1);
    assertResult(new SelectionAccessTest(test, Access.DEFAULT), AccessSelectorTest.class, 1);
    assertResult(new SelectionAccessTest(test, Access.PRIVATE), AccessSelectorTest.class, 1);

    assertResult(new SelectionAccessTest(test, Access.LIKE_DEFAULT), AccessSelectorTest.class, 3);
    assertResult(new SelectionAccessTest(test, Access.LIKE_PROTECTED), AccessSelectorTest.class, 2);
  }

  static class ParameterSelectorTest {
    ParameterSelectorTest(String s){}
    ParameterSelectorTest(int i, int j){}
    ParameterSelectorTest(double a, double b, double c){}
    ParameterSelectorTest(long a){}
  }

  @Test
  public void testParameterSelector() {
    assertNotNull(reflect().constructor().withParameters(String.class).in(ParameterSelectorTest.class));
    assertNotNull(reflect().constructor().withParameters(int.class, int.class).in(ParameterSelectorTest.class));
    assertNotNull(reflect().constructor().withParameters(double.class, double.class, double.class).in(ParameterSelectorTest.class));
    assertNotNull(reflect().constructor().withParameters(long.class).in(ParameterSelectorTest.class));

    Constructor<?> constructor = reflect().constructor().withParameters(String.class).in(ParameterSelectorTest.class);
    Class[] parameters = constructor.getParameterTypes();
    assertArrayEquals(new Class[]{String.class}, parameters);

    constructor = reflect().constructor().withParameters(long.class).in(ParameterSelectorTest.class);
    parameters = constructor.getParameterTypes();
    assertArrayEquals(new Class[]{long.class}, parameters);
  }

}
