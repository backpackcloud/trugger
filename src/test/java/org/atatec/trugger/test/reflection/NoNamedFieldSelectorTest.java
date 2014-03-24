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

import org.atatec.trugger.reflection.ReflectionPredicates;
import org.atatec.trugger.selector.FieldSelector;
import org.atatec.trugger.test.Flag;
import org.atatec.trugger.test.SelectionTestAdapter;
import org.junit.Test;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.atatec.trugger.reflection.Reflection.reflect;
import static org.atatec.trugger.test.TruggerTest.*;
import static org.junit.Assert.*;


/**
 * @author Marcelo Varella Barca Guimarães
 *
 */
public class NoNamedFieldSelectorTest {

  static class SimpleObject {
    private int i;
  }

  @Test
  public void simpleTest() {
    assertResult(reflect().field().in(SimpleObject.class));
  }

  static class AnnotatedSelectorTest {
    @Flag
    @Resource
    private String annotatedField;
    private String notAnnotatedField;
  }

  @Test
  public void testAnnotatedSelector() {
    assertResult(new SelectionTestAdapter<FieldSelector, Field>(){
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.annotated();
      }
      public void assertions(Field field) {
        assertTrue(field.isAnnotationPresent(Flag.class));
      }
    }, AnnotatedSelectorTest.class);
  }

  @Test
  public void testNotAnnotatedSelector() {
    assertResult(new SelectionTestAdapter<FieldSelector, Field>(){
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.notAnnotated();
      }
      public void assertions(Field field) {
        assertFalse(field.isAnnotationPresent(Flag.class));
      }
    }, AnnotatedSelectorTest.class);
  }

  @Test
  public void testAnnotatedWithSelector() {
    assertResult(new SelectionTestAdapter<FieldSelector, Field>(){
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.annotatedWith(Flag.class);
      }
      public void assertions(Field field) {
        assertTrue(field.isAnnotationPresent(Flag.class));
      }
    }, AnnotatedSelectorTest.class);
  }

  @Test
  public void testNotAnnotatedWithSelector() {
    assertResult(new SelectionTestAdapter<FieldSelector, Field>(){
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
      public void assertions(Field field) {
        assertFalse(field.isAnnotationPresent(Flag.class));
      }
    }, AnnotatedSelectorTest.class);
  }

  static class NonFinalSelectorTest {
    final String finalField = null;
    String nonFinalField;
  }

  @Test
  public void testNonFinalSelector() {
    assertResult(new SelectionTestAdapter<FieldSelector, Field>(){
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.nonFinal();
      }
      public void assertions(Field field) {
        assertMatch(field, ReflectionPredicates.dontDeclare(Modifier.FINAL));
      }
    }, NonFinalSelectorTest.class);
  }

  static class NonStaticSelectorTest {
    static String staticField;
    String nonStaticField;
  }

  @Test
  public void testNonStaticSelector() {
    assertResult(new SelectionTestAdapter<FieldSelector, Field>(){
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.nonStatic();
      }
      public void assertions(Field field) {
        assertMatch(field, ReflectionPredicates.dontDeclare(Modifier.STATIC));
      }
    }, NonStaticSelectorTest.class);
  }

  static class BaseClassTest {
    int i;
  }

  static class RecursivelySelectorTest extends BaseClassTest {

  }

  @Test
  public void testRecursivelySelector() {
    assertNull(reflect().field().in(RecursivelySelectorTest.class));
    assertNotNull(reflect().field().recursively().in(RecursivelySelectorTest.class));
  }

  @Test
  public void testPredicateSelector() {
    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.filter(el -> true);
      }
    }, SimpleObject.class);
    assertNoResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.filter(el -> false);
      }
    }, SimpleObject.class);
  }

  static class TypeSelectorTest {
    int i;
    Integer I;
    double d;
    Double D;
    String s;
  }

  @Test
  public void testTypeSelector() {
    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.ofType(int.class);
      }
    }, TypeSelectorTest.class);
    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.ofType(Integer.class);
      }
    }, TypeSelectorTest.class);

    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.ofType(double.class);
      }
    }, TypeSelectorTest.class);
    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.ofType(Double.class);
      }
    }, TypeSelectorTest.class);

    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.ofType(String.class);
      }
    }, TypeSelectorTest.class);
    assertNoResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.ofType(Object.class);
      }
    }, TypeSelectorTest.class);
  }

  static class AssignableSelectorTest {
    int i;
    double d;
    String s;
  }

  @Test
  public void testAssignableToSelector() {
    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.assignableTo(Integer.class);
      }
    }, AssignableSelectorTest.class);

    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.assignableTo(int.class);
      }
    }, AssignableSelectorTest.class);

    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.assignableTo(double.class);
      }
    }, AssignableSelectorTest.class);
    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.assignableTo(Double.class);
      }
    }, AssignableSelectorTest.class);

    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.assignableTo(String.class);
      }
    }, AssignableSelectorTest.class);
  }

}
