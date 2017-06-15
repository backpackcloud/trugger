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
package tools.devnull.trugger.reflection;

import org.junit.Test;
import tools.devnull.trugger.Flag;

import java.lang.reflect.Constructor;
import java.util.function.Predicate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tools.devnull.trugger.reflection.Reflection.reflect;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ConstructorSelectorTest {

	static class TestObject {
		@Flag
    TestObject() {}
		public TestObject(int i) {}
	}

	@Test(expected = ReflectionException.class)
	public void testInsufficientSelector() {
	  reflect().constructor().in(TestObject.class);
	}

	@Test
	public void testVisibleSelector() {
    assertNotNull(reflect().visible().constructor().in(TestObject.class));
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

  @Test
  public void testConstructorPredicate() {
    Predicate<Constructor> predicate = mock(Predicate.class);

    assertNull(reflect().constructor().filter(predicate).in(ParameterSelectorTest.class));
    verify(predicate, atLeastOnce()).test(any(Constructor.class));

    when(predicate.test(any(Constructor.class))).thenReturn(true);
    assertNotNull(reflect().constructor().filter(predicate).in(ParameterSelectorTest.class));
    verify(predicate, atLeastOnce()).test(any(Constructor.class));
  }

}
