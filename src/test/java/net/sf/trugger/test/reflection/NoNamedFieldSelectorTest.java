/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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

import static net.sf.trugger.reflection.Access.DEFAULT;
import static net.sf.trugger.reflection.Access.LIKE_DEFAULT;
import static net.sf.trugger.reflection.Access.PRIVATE;
import static net.sf.trugger.reflection.Access.PROTECTED;
import static net.sf.trugger.reflection.Access.PUBLIC;
import static net.sf.trugger.reflection.Reflection.reflect;
import static net.sf.trugger.test.TruggerTest.assertMatch;
import static net.sf.trugger.test.TruggerTest.assertNoResult;
import static net.sf.trugger.test.TruggerTest.assertResult;
import static net.sf.trugger.test.TruggerTest.assertThrow;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import javax.annotation.Resource;

import net.sf.trugger.predicate.Predicates;
import net.sf.trugger.reflection.ReflectionException;
import net.sf.trugger.reflection.ReflectionPredicates;
import net.sf.trugger.selector.FieldSelector;
import net.sf.trugger.test.Flag;
import net.sf.trugger.test.SelectionTestAdapter;

import org.junit.Test;


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
        assertMatch(field, ReflectionPredicates.NON_FINAL);
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
        assertMatch(field, ReflectionPredicates.NON_STATIC);
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
        selector.thatMatches(Predicates.ALWAYS_TRUE);
      }
    }, SimpleObject.class);
    assertNoResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field();
      }
      public void makeSelections(FieldSelector selector) {
        selector.thatMatches(Predicates.ALWAYS_FALSE);
      }
    }, SimpleObject.class);
  }

  static class AccessSelectorTest {
    private int privateField;
    protected int protectedField;
    int defaultField;
    public int publicField;
  }

  @Test
  public void testAccessSelector() {
    assertResult(reflect().field().withAccess(PRIVATE).in(AccessSelectorTest.class));
    assertResult(reflect().field().withAccess(PROTECTED).in(AccessSelectorTest.class));
    assertResult(reflect().field().withAccess(DEFAULT).in(AccessSelectorTest.class));
    assertResult(reflect().field().withAccess(PUBLIC).in(AccessSelectorTest.class));

    assertThrow(new Runnable(){
      public void run() {
        reflect().field().withAccess(LIKE_DEFAULT).in(AccessSelectorTest.class);
      }
    }, ReflectionException.class);
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

  @Test(expected = ReflectionException.class)
  public void testMultipleMatch() {
    reflect().field().assignableTo(Object.class).in(AssignableSelectorTest.class);
  }

}
