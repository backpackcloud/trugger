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

import org.atatec.trugger.predicate.Predicates;
import org.atatec.trugger.reflection.ReflectionPredicates;
import org.atatec.trugger.selector.ConstructorsSelector;
import org.atatec.trugger.test.Flag;
import org.atatec.trugger.test.SelectionTestAdapter;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.Set;

import static org.atatec.trugger.reflection.Reflection.reflect;
import static org.atatec.trugger.test.TruggerTest.assertMatch;
import static org.atatec.trugger.test.TruggerTest.assertNoResult;
import static org.atatec.trugger.test.TruggerTest.assertResult;
import static org.junit.Assert.assertTrue;

/** @author Marcelo Varella Barca Guimarães */
public class ConstructorsSelectorTest {

  static class AnnotatedSelectorTest {
    @Flag
    AnnotatedSelectorTest() {
    }

    public AnnotatedSelectorTest(int i) {
    }
  }

  @Test
  public void testNoSelector() {
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>() {
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }
    }, AnnotatedSelectorTest.class, 2);
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>() {
      public ConstructorsSelector createSelector() {
        return reflect().visible().constructors();
      }
    }, AnnotatedSelectorTest.class, 1);
  }

  @Test
  public void testAnnoatedSelector() {
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>() {
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }

      public void makeSelections(ConstructorsSelector selector) {
        selector.annotated();
      }

      public void assertions(Set<Constructor> set) {
        assertMatch(set, ReflectionPredicates.IS_ANNOTATED);
      }
    }, AnnotatedSelectorTest.class, 1);
    assertTrue(reflect().visible().constructors().annotated().in(AnnotatedSelectorTest.class).isEmpty());
  }

  @Test
  public void testNotAnnoatedSelector() {
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>() {
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }

      public void makeSelections(ConstructorsSelector selector) {
        selector.notAnnotated();
      }

      public void assertions(Set<Constructor> set) {
        assertMatch(set, ReflectionPredicates.IS_NOT_ANNOTATED);
      }
    }, AnnotatedSelectorTest.class, 1);

    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>() {
      public ConstructorsSelector createSelector() {
        return reflect().visible().constructors();
      }

      public void makeSelections(ConstructorsSelector selector) {
        selector.notAnnotated();
      }

      public void assertions(Set<Constructor> set) {
        assertMatch(set, ReflectionPredicates.IS_NOT_ANNOTATED);
      }
    }, AnnotatedSelectorTest.class, 1);
  }

  @Test
  public void testAnnoatedWithSelector() {
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>() {
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }

      public void makeSelections(ConstructorsSelector selector) {
        selector.annotatedWith(Flag.class);
      }

      public void assertions(Set<Constructor> set) {
        assertMatch(set, ReflectionPredicates.isAnnotatedWith(Flag.class));
      }
    }, AnnotatedSelectorTest.class, 1);
    assertTrue(
      reflect().visible().constructors().annotatedWith(Flag.class)
        .in(AnnotatedSelectorTest.class).isEmpty());
  }

  @Test
  public void testNotAnnoatedWithSelector() {
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>() {
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }

      public void makeSelections(ConstructorsSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }

      public void assertions(Set<Constructor> set) {
        assertMatch(set, ReflectionPredicates.isNotAnnotatedWith(Flag.class));
      }
    }, AnnotatedSelectorTest.class, 1);
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>() {
      public ConstructorsSelector createSelector() {
        return reflect().visible().constructors();
      }

      public void makeSelections(ConstructorsSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }

      public void assertions(Set<Constructor> set) {
        assertMatch(set, ReflectionPredicates.isNotAnnotatedWith(Flag.class));
      }
    }, AnnotatedSelectorTest.class, 1);
  }

  @Test
  public void testPredicateSelector() {
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>() {
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }

      public void makeSelections(ConstructorsSelector selector) {
        selector.that(Predicates.ALWAYS_TRUE);
      }
    }, AnnotatedSelectorTest.class, 2);

    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>() {
      public ConstructorsSelector createSelector() {
        return reflect().visible().constructors();
      }

      public void makeSelections(ConstructorsSelector selector) {
        selector.that(Predicates.ALWAYS_TRUE);
      }
    }, AnnotatedSelectorTest.class, 1);

    assertNoResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>() {
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }

      public void makeSelections(ConstructorsSelector selector) {
        selector.that(Predicates.ALWAYS_FALSE);
      }
    }, AnnotatedSelectorTest.class);

    assertNoResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>() {
      public ConstructorsSelector createSelector() {
        return reflect().visible().constructors();
      }

      public void makeSelections(ConstructorsSelector selector) {
        selector.that(Predicates.ALWAYS_FALSE);
      }
    }, AnnotatedSelectorTest.class);
  }

}
