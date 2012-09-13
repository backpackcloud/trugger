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
import static org.atatec.trugger.test.TruggerTest.assertMatch;
import static org.atatec.trugger.test.TruggerTest.assertNoResult;
import static org.atatec.trugger.test.TruggerTest.assertResult;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.Set;

import javax.annotation.Resource;

import org.atatec.trugger.predicate.Predicates;
import org.atatec.trugger.reflection.ReflectionPredicates;
import org.atatec.trugger.selector.SetterMethodSelector;
import org.atatec.trugger.test.Flag;
import org.atatec.trugger.test.SelectionTestAdapter;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class SetterMethodSelectorTest {

  private static final String HITS = "hits";
  private static final String COUNT = "count";

  private int count;
  private int hits;

  @Flag
  @Resource
  public void setCount(int count) {
    this.count = count;
  }

  public void setHits(int hits) {
    this.hits = hits;
  }

  public void setHits(long hits) {
    this.hits = (int) hits;
  }

  @Test
  public void testNoSelector() {
    assertResult(new SelectionTestAdapter<SetterMethodSelector, Set<Method>>() {
      public SetterMethodSelector createSelector() {
        return reflect().setterOf(HITS);
      }
    }, this, 2);
    assertResult(new SelectionTestAdapter<SetterMethodSelector, Set<Method>>() {
      public SetterMethodSelector createSelector() {
        return reflect().setterOf(COUNT);
      }
    }, this, 1);
    assertNoResult(new SelectionTestAdapter<SetterMethodSelector, Set<Method>>() {
      public SetterMethodSelector createSelector() {
        return reflect().setterOf("myField");
      }
    }, this);
  }

  @Test
  public void testAnnotatedSelector() {
    assertResult(new SelectionTestAdapter<SetterMethodSelector, Set<Method>>() {
      public SetterMethodSelector createSelector() {
        return reflect().setterOf(COUNT);
      }
      public void makeSelections(SetterMethodSelector selector) {
        selector.annotated();
      }
      public void assertions(Set<Method> methods) {
        assertMatch(methods, ReflectionPredicates.ANNOTATED);
      }
    }, this, 1);
    assertNoResult(new SelectionTestAdapter<SetterMethodSelector, Set<Method>>() {
      public SetterMethodSelector createSelector() {
        return reflect().setterOf(HITS);
      }
      public void makeSelections(SetterMethodSelector selector) {
        selector.annotated();
      }
    }, this);
  }

  @Test
  public void testNotAnnotatedSelector() {
    assertResult(new SelectionTestAdapter<SetterMethodSelector, Set<Method>>() {
      public SetterMethodSelector createSelector() {
        return reflect().setterOf(HITS);
      }
      public void makeSelections(SetterMethodSelector selector) {
        selector.notAnnotated();
      }
      public void assertions(Set<Method> methods) {
        assertMatch(methods, ReflectionPredicates.NOT_ANNOTATED);
      }
    }, this, 2);
    assertNoResult(new SelectionTestAdapter<SetterMethodSelector, Set<Method>>() {
      public SetterMethodSelector createSelector() {
        return reflect().setterOf(COUNT);
      }
      public void makeSelections(SetterMethodSelector selector) {
        selector.notAnnotated();
      }
    }, this);
  }

  @Test
  public void testAnnotatedWithSelector() {
    assertResult(new SelectionTestAdapter<SetterMethodSelector, Set<Method>>() {
      public SetterMethodSelector createSelector() {
        return reflect().setterOf(COUNT);
      }
      public void makeSelections(SetterMethodSelector selector) {
        selector.annotatedWith(Flag.class);
      }
      public void assertions(Set<Method> methods) {
        assertMatch(methods, ReflectionPredicates.isAnnotatedWith(Flag.class));
      }
    }, this, 1);
    assertNoResult(new SelectionTestAdapter<SetterMethodSelector, Set<Method>>() {
      public SetterMethodSelector createSelector() {
        return reflect().setterOf(HITS);
      }
      public void makeSelections(SetterMethodSelector selector) {
        selector.annotatedWith(Flag.class);
      }
    }, this);
  }

  @Test
  public void testNotAnnotatedWithSelector() {
    assertResult(new SelectionTestAdapter<SetterMethodSelector, Set<Method>>() {
      public SetterMethodSelector createSelector() {
        return reflect().setterOf(HITS);
      }
      public void makeSelections(SetterMethodSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
      public void assertions(Set<Method> methods) {
        assertMatch(methods, ReflectionPredicates.isNotAnnotatedWith(Flag.class));
      }
    }, this, 2);
    assertNoResult(new SelectionTestAdapter<SetterMethodSelector, Set<Method>>() {
      public SetterMethodSelector createSelector() {
        return reflect().setterOf(COUNT);
      }
      public void makeSelections(SetterMethodSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
    }, this);
  }

  @Test
  public void testPredicateSelector() {
    assertResult(new SelectionTestAdapter<SetterMethodSelector, Set<Method>>() {
      public SetterMethodSelector createSelector() {
        return reflect().setterOf(HITS);
      }
      public void makeSelections(SetterMethodSelector selector) {
        selector.that(Predicates.ALWAYS_TRUE);
      }
    }, this, 2);
    assertNoResult(new SelectionTestAdapter<SetterMethodSelector, Set<Method>>() {
      public SetterMethodSelector createSelector() {
        return reflect().setterOf(HITS);
      }
      public void makeSelections(SetterMethodSelector selector) {
        selector.that(Predicates.ALWAYS_FALSE);
      }
    }, this);
  }

  @Test
  public void testRecursivelySelector() {
    SetterMethodSelectorTest object = new SetterMethodSelectorTest(){};
    assertNoResult(reflect().setterOf("count").in(object));
    assertResult(reflect().setterOf("count").recursively().in(object), 1);
  }

  @Test
  public void testTypeSelector() {
    assertNull(reflect().setterOf("hits").forType(String.class).in(this));
    Method setter = reflect().setterOf("hits").forType(int.class).in(this);
    assertNotNull(setter);
    assertArrayEquals(new Class[]{int.class}, setter.getParameterTypes());

    setter = reflect().setterOf("hits").forType(long.class).in(this);
    assertNotNull(setter);
    assertArrayEquals(new Class[]{long.class}, setter.getParameterTypes());
  }

}
