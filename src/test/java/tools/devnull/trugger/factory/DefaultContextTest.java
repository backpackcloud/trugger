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

package tools.devnull.trugger.factory;

import org.junit.Test;
import tools.devnull.kodo.Spec;
import tools.devnull.trugger.util.factory.DefaultContext;

import java.lang.reflect.Parameter;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tools.devnull.kodo.Expectation.the;
import static tools.devnull.kodo.Expectation.to;
import static tools.devnull.trugger.TruggerTest.contain;

public class DefaultContextTest {

  @Test
  public void testFunctionality() {
    Function<Parameter, Object> functionA = mock(Function.class);
    Predicate<Parameter> conditionA = mock(Predicate.class);
    Object resultA = new Object();
    when(functionA.apply(any())).thenReturn(resultA);
    when(conditionA.test(any())).thenReturn(false);

    Function<Parameter, Object> functionB = mock(Function.class);
    Predicate<Parameter> conditionB = mock(Predicate.class);
    Object resultB = new Object();
    when(functionB.apply(any())).thenReturn(resultB);
    when(conditionB.test(any())).thenReturn(true);

    Spec.given(new DefaultContext())
        .when(context -> context.use(functionA).when(conditionA).
            use(functionB).when(conditionB))

        // don't need an actual parameter since everything here is a mock
        .expect(context -> context.resolve(null), to(contain(resultB)))

        .expect(the(conditionA), to().be(tested()))
        .expect(the(conditionB), to().be(tested()))
        .expect(the(functionA), to().not().be(used()))
        .expect(the(functionB), to().be(used()));
  }

  @Test
  public void testDefaultFunction() {
    Function<Parameter, Object> functionA = mock(Function.class);
    Predicate<Parameter> conditionA = mock(Predicate.class);
    Object resultA = new Object();
    when(functionA.apply(any())).thenReturn(resultA);
    when(conditionA.test(any())).thenReturn(false);

    Function<Parameter, Object> functionB = mock(Function.class);
    Object resultB = new Object();
    when(functionB.apply(any())).thenReturn(resultB);

    Spec.given(new DefaultContext())
        .when(context -> context.use(functionA).when(conditionA).
            use(functionB).byDefault())

        // don't need an actual parameter since everything here is a mock
        .expect(context -> context.resolve(null), to(contain(resultB)))

        .expect(the(conditionA), to().be(tested()))
        .expect(the(functionA), to().not().be(used()))
        .expect(the(functionB), to().be(used()));
  }

  private Predicate<Predicate> tested() {
    return predicate -> {
      try {
        verify(predicate).test(null);
        return true;
      } catch (Throwable e) {
        return false;
      }
    };
  }

  private Predicate<Function> used() {
    return function -> {
      try {
        verify(function).apply(null);
        return true;
      } catch (Throwable e) {
        return false;
      }
    };
  }

}
