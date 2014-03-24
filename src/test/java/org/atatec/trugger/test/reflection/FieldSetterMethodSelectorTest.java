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

import org.atatec.trugger.selector.FieldSetterMethodSelector;
import org.atatec.trugger.test.Flag;
import org.atatec.trugger.test.SelectionTestAdapter;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.atatec.trugger.reflection.Reflection.field;
import static org.atatec.trugger.reflection.Reflection.reflect;
import static org.atatec.trugger.test.TruggerTest.assertNoResult;
import static org.atatec.trugger.test.TruggerTest.assertResult;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class FieldSetterMethodSelectorTest {

  private int count;
  private int hits;
  private int size;

  @Flag
  @Resource
  public void setCount(int count) {
    this.count = count;
  }

  public void setHits(int hits) {
    this.hits = hits;
  }

  @Resource
  public void setSize(int size) {
    this.size = size;
  }

  private Field fieldCount;
  private Field fieldHits;
  private Field fieldSize;

  @Before
  public void initialize() {
    fieldCount = field("count").in(this);
    fieldHits = field("hits").in(this);
    fieldSize = field("size").in(this);
  }

  @Test
  public void testNoSelector() {
    assertResult(new SelectionTestAdapter<FieldSetterMethodSelector, Method>() {
      public FieldSetterMethodSelector createSelector() {
        return reflect().setterOf(fieldHits);
      }
    }, this);
    assertResult(new SelectionTestAdapter<FieldSetterMethodSelector, Method>() {
      public FieldSetterMethodSelector createSelector() {
        return reflect().setterOf(fieldCount);
      }
    }, this);
    assertNoResult(new SelectionTestAdapter<FieldSetterMethodSelector, Method>() {
      public FieldSetterMethodSelector createSelector() {
        return reflect().setterOf(fieldCount);
      }
    }, Object.class);
  }

  @Test
  public void testAnnotatedSelector() {
    assertResult(new SelectionTestAdapter<FieldSetterMethodSelector, Method>() {
      public FieldSetterMethodSelector createSelector() {
        return reflect().setterOf(fieldCount);
      }
      public void makeSelections(FieldSetterMethodSelector selector) {
        selector.annotated();
      }
      public void assertions(Method method) {
        assertTrue(method.getDeclaredAnnotations().length > 0);
      }
    }, this);
    assertNoResult(new SelectionTestAdapter<FieldSetterMethodSelector, Method>() {
      public FieldSetterMethodSelector createSelector() {
        return reflect().setterOf(fieldHits);
      }
      public void makeSelections(FieldSetterMethodSelector selector) {
        selector.annotated();
      }
    }, this);
  }

  @Test
  public void testNotAnnotatedSelector() {
    assertResult(new SelectionTestAdapter<FieldSetterMethodSelector, Method>() {
      public FieldSetterMethodSelector createSelector() {
        return reflect().setterOf(fieldHits);
      }
      public void makeSelections(FieldSetterMethodSelector selector) {
        selector.notAnnotated();
      }
      public void assertions(Method method) {
        assertTrue(method.getDeclaredAnnotations().length == 0);
      }
    }, this);
    assertNoResult(new SelectionTestAdapter<FieldSetterMethodSelector, Method>() {
      public FieldSetterMethodSelector createSelector() {
        return reflect().setterOf(fieldCount);
      }
      public void makeSelections(FieldSetterMethodSelector selector) {
        selector.notAnnotated();
      }
    }, this);
  }

  @Test
  public void testAnnotatedWithSelector() {
    assertResult(new SelectionTestAdapter<FieldSetterMethodSelector, Method>() {
      public FieldSetterMethodSelector createSelector() {
        return reflect().setterOf(fieldCount);
      }
      public void makeSelections(FieldSetterMethodSelector selector) {
        selector.annotatedWith(Flag.class);
      }
      public void assertions(Method method) {
        assertTrue(method.isAnnotationPresent(Flag.class));
      }
    }, this);
    assertNoResult(new SelectionTestAdapter<FieldSetterMethodSelector, Method>() {
      public FieldSetterMethodSelector createSelector() {
        return reflect().setterOf(fieldHits);
      }
      public void makeSelections(FieldSetterMethodSelector selector) {
        selector.annotatedWith(Flag.class);
      }
    }, this);
  }

  @Test
  public void testNotAnnotatedWithSelector() {
    assertResult(new SelectionTestAdapter<FieldSetterMethodSelector, Method>() {
      public FieldSetterMethodSelector createSelector() {
        return reflect().setterOf(fieldHits);
      }
      public void makeSelections(FieldSetterMethodSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
      public void assertions(Method method) {
        assertFalse(method.isAnnotationPresent(Flag.class));
      }
    }, this);
    assertResult(new SelectionTestAdapter<FieldSetterMethodSelector, Method>() {
      public FieldSetterMethodSelector createSelector() {
        return reflect().setterOf(fieldSize);
      }
      public void makeSelections(FieldSetterMethodSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
      public void assertions(Method method) {
        assertFalse(method.isAnnotationPresent(Flag.class));
      }
    }, this);
    assertNoResult(new SelectionTestAdapter<FieldSetterMethodSelector, Method>() {
      public FieldSetterMethodSelector createSelector() {
        return reflect().setterOf(fieldCount);
      }
      public void makeSelections(FieldSetterMethodSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
    }, this);
  }

  @Test
  public void testPredicateSelector() {
    assertResult(new SelectionTestAdapter<FieldSetterMethodSelector, Method>() {
      public FieldSetterMethodSelector createSelector() {
        return reflect().setterOf(fieldSize);
      }
      public void makeSelections(FieldSetterMethodSelector selector) {
        selector.filter(el -> true);
      }
    }, this);
    assertNoResult(new SelectionTestAdapter<FieldSetterMethodSelector, Method>() {
      public FieldSetterMethodSelector createSelector() {
        return reflect().setterOf(fieldCount);
      }
      public void makeSelections(FieldSetterMethodSelector selector) {
        selector.filter(el -> false);
      }
    }, this);
  }

}
