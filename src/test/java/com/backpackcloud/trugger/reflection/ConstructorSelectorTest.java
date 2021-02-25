/*
 * The Apache License
 *
 * Copyright 2009 Marcelo Guimaraes <ataxexe@backpackcloud.com>
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
package com.backpackcloud.trugger.reflection;

import org.junit.Test;
import com.backpackcloud.trugger.Flag;

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

/**
 * @author Marcelo Guimaraes
 */
public class ConstructorSelectorTest {

	static class TestObject {
		@Flag
    TestObject() {}
		public TestObject(int i) {}
	}

	@Test(expected = ReflectionException.class)
	public void testInsufficientSelector() {
	  Reflection.reflect().constructor().from(TestObject.class);
	}

	@Test
	public void testVisibleSelector() {
    assertNotNull(Reflection.reflect().visible().constructor().from(TestObject.class).orElse(null));
	}

  @Test
  public void testPredicateSelector() {
    assertNotNull(Reflection.reflect().constructor().filter((c) -> true).withoutParameters().from(Object.class).orElse(null));
    assertNull(Reflection.reflect().constructor().filter((c) -> false).withoutParameters().from(Object.class).orElse(null));
  }

  static class ParameterSelectorTest {
    ParameterSelectorTest(String s){}
    ParameterSelectorTest(int i, int j){}
    ParameterSelectorTest(double a, double b, double c){}
    ParameterSelectorTest(long a){}
  }

  @Test
  public void testParameterSelector() {
    assertNotNull(Reflection.reflect().constructor().withParameters(String.class).from(ParameterSelectorTest.class).orElse(null));
    assertNotNull(Reflection.reflect().constructor().withParameters(int.class, int.class).from(ParameterSelectorTest.class).orElse(null));
    assertNotNull(Reflection.reflect().constructor().withParameters(double.class, double.class, double.class).from(ParameterSelectorTest.class).orElse(null));
    assertNotNull(Reflection.reflect().constructor().withParameters(long.class).from(ParameterSelectorTest.class).orElse(null));

    ReflectedConstructor constructor = Reflection.reflect()
        .constructor()
        .withParameters(String.class)
        .from(ParameterSelectorTest.class)
        .get();

    Class[] parameters = constructor.getParameterTypes();
    assertArrayEquals(new Class[]{String.class}, parameters);

    constructor = Reflection.reflect().constructor().withParameters(long.class).from(ParameterSelectorTest.class).get();
    parameters = constructor.getParameterTypes();
    assertArrayEquals(new Class[]{long.class}, parameters);
  }

  @Test
  public void testConstructorPredicate() {
    Predicate<Constructor> predicate = mock(Predicate.class);

    assertNull(Reflection.reflect().constructor().filter(predicate).from(ParameterSelectorTest.class).orElse(null));
    verify(predicate, atLeastOnce()).test(any(Constructor.class));

    when(predicate.test(any(Constructor.class))).thenReturn(true);
    assertNotNull(Reflection.reflect().constructor().filter(predicate).from(ParameterSelectorTest.class).orElse(null));
    verify(predicate, atLeastOnce()).test(any(Constructor.class));
  }

}
