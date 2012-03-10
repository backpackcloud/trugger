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
package net.sf.trugger.test.reflection;

import static net.sf.trugger.reflection.Reflection.reflect;
import static net.sf.trugger.test.TruggerTest.assertNoResult;
import static net.sf.trugger.test.TruggerTest.assertResult;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import net.sf.trugger.predicate.Predicates;
import net.sf.trugger.selector.GetterMethodSelector;
import net.sf.trugger.test.Flag;
import net.sf.trugger.test.SelectionTestAdapter;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class GetterMethodSelectorTest {

  private int count;
  private int hits;

  @Flag
  @Resource
  public int getCount() {
    return count;
  }

  public int getHits() {
    return hits;
  }

  @Test
  public void testNoSelector() {
    assertResult(new SelectionTestAdapter<GetterMethodSelector, Method>() {
      public GetterMethodSelector createSelector() {
        return reflect().getterFor("count");
      }
    }, this);
    assertResult(new SelectionTestAdapter<GetterMethodSelector, Method>() {
      public GetterMethodSelector createSelector() {
        return reflect().getterFor("hits");
      }
    }, this);
    assertNoResult(new SelectionTestAdapter<GetterMethodSelector, Method>() {
      public GetterMethodSelector createSelector() {
        return reflect().getterFor("myField");
      }
    }, this);
  }

  @Test
  public void testAnnotatedSelector() {
    assertResult(new SelectionTestAdapter<GetterMethodSelector, Method>() {
      public GetterMethodSelector createSelector() {
        return reflect().getterFor("count");
      }
      public void makeSelections(GetterMethodSelector selector) {
        selector.annotated();
      }
      public void assertions(Method method) {
        assertTrue(method.getDeclaredAnnotations().length > 0);
      }
    }, this);

    assertNoResult(new SelectionTestAdapter<GetterMethodSelector, Method>() {
      public GetterMethodSelector createSelector() {
        return reflect().getterFor("hits");
      }
      public void makeSelections(GetterMethodSelector selector) {
        selector.annotated();
      }
    }, this);
  }

  @Test
  public void testNotAnnotatedSelector() {
    assertResult(new SelectionTestAdapter<GetterMethodSelector, Method>() {
      public GetterMethodSelector createSelector() {
        return reflect().getterFor("hits");
      }
      public void makeSelections(GetterMethodSelector selector) {
        selector.notAnnotated();
      }
      public void assertions(Method method) {
        assertEquals(0, method.getDeclaredAnnotations().length);
      }
    }, this);

    assertNoResult(new SelectionTestAdapter<GetterMethodSelector, Method>() {
      public GetterMethodSelector createSelector() {
        return reflect().getterFor("count");
      }
      public void makeSelections(GetterMethodSelector selector) {
        selector.notAnnotated();
      }
    }, this);
  }

  @Test
  public void testAnnotatedWithSelector() {
    assertResult(new SelectionTestAdapter<GetterMethodSelector, Method>() {
      public GetterMethodSelector createSelector() {
        return reflect().getterFor("count");
      }
      public void makeSelections(GetterMethodSelector selector) {
        selector.annotatedWith(Flag.class);
      }
      public void assertions(Method method) {
        assertTrue(method.isAnnotationPresent(Flag.class));
      }
    }, this);

    assertNoResult(new SelectionTestAdapter<GetterMethodSelector, Method>() {
      public GetterMethodSelector createSelector() {
        return reflect().getterFor("hits");
      }
      public void makeSelections(GetterMethodSelector selector) {
        selector.annotatedWith(Flag.class);
      }
    }, this);
  }

  @Test
  public void testNotAnnotatedWithSelector() {
    assertResult(new SelectionTestAdapter<GetterMethodSelector, Method>() {
      public GetterMethodSelector createSelector() {
        return reflect().getterFor("hits");
      }
      public void makeSelections(GetterMethodSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
      public void assertions(Method method) {
        assertFalse(method.isAnnotationPresent(Flag.class));
      }
    }, this);

    assertNoResult(new SelectionTestAdapter<GetterMethodSelector, Method>() {
      public GetterMethodSelector createSelector() {
        return reflect().getterFor("count");
      }
      public void makeSelections(GetterMethodSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
    }, this);
  }

  @Test
  public void testPredicateSelector() {
    assertResult(new SelectionTestAdapter<GetterMethodSelector, Method>() {
      public GetterMethodSelector createSelector() {
        return reflect().getterFor("hits");
      }
      public void makeSelections(GetterMethodSelector selector) {
        selector.thatMatches(Predicates.ALWAYS_TRUE);
      }
      public void assertions(Method method) {
        assertFalse(method.isAnnotationPresent(Flag.class));
      }
    }, this);

    assertNoResult(new SelectionTestAdapter<GetterMethodSelector, Method>() {
      public GetterMethodSelector createSelector() {
        return reflect().getterFor("count");
      }
      public void makeSelections(GetterMethodSelector selector) {
        selector.thatMatches(Predicates.ALWAYS_FALSE);
      }
    }, this);
  }

  @Test
  public void testRecursivelySelector() {
    GetterMethodSelectorTest object = new GetterMethodSelectorTest(){};
    assertNull(reflect().getterFor("count").in(object));
    assertNotNull(reflect().getterFor("count").recursively().in(object));
  }

}
