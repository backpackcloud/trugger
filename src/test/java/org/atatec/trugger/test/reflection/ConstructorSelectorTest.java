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

import org.atatec.trugger.reflection.ReflectionException;
import org.atatec.trugger.selector.ConstructorSelector;
import org.atatec.trugger.test.Flag;
import org.atatec.trugger.test.SelectionTest;
import org.atatec.trugger.test.SelectionTestAdapter;
import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.atatec.trugger.reflection.Reflection.reflect;
import static org.atatec.trugger.test.TruggerTest.assertResult;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ConstructorSelectorTest {

	static class AnnotatedSelectorTest {
		@Flag
		AnnotatedSelectorTest() {}
		public AnnotatedSelectorTest(int i) {}
	}

	@Test
  public void testNoSelector() {
    assertResult(new SelectionTestAdapter(){
      public Object createSelector() {
        return reflect().constructor();
      }
    }, Object.class);
    assertResult(new SelectionTestAdapter(){
      public Object createSelector() {
        return reflect().visible().constructor();
      }
    }, Object.class);
  }

	@Test(expected = ReflectionException.class)
	public void testInsufficientSelector() {
	  reflect().constructor().in(AnnotatedSelectorTest.class);
	}

	@Test
	public void testVisibleSelector() {
    assertNotNull(reflect().visible().constructor().in(AnnotatedSelectorTest.class));
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
    assertNull(reflect().visible().constructor().annotated().in(AnnotatedSelectorTest.class));
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
    assertNotNull(reflect().visible().constructor().notAnnotated().in(AnnotatedSelectorTest.class));
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
    assertNull(reflect().visible().constructor().annotatedWith(Flag.class).in(AnnotatedSelectorTest.class));
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
    assertNotNull(reflect().visible().constructor().notAnnotatedWith(Flag.class).in(AnnotatedSelectorTest.class));
  }

  @Test
  public void testPredicateSelector() {
    assertNotNull(reflect().constructor().filter((c) -> true).withoutParameters().in(Object.class));
    assertNull(reflect().constructor().filter((c) -> false).withoutParameters().in(Object.class));
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
