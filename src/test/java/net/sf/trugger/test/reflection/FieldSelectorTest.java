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
import static net.sf.trugger.test.TruggerTest.assertAccessSelector;
import static net.sf.trugger.test.TruggerTest.assertMatch;
import static net.sf.trugger.test.TruggerTest.assertNoResult;
import static net.sf.trugger.test.TruggerTest.assertResult;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import javax.annotation.Resource;

import net.sf.trugger.predicate.Predicates;
import net.sf.trugger.reflection.Access;
import net.sf.trugger.reflection.ReflectionPredicates;
import net.sf.trugger.selector.FieldSelector;
import net.sf.trugger.test.Flag;
import net.sf.trugger.test.SelectionTestAdapter;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class FieldSelectorTest {

  static class AnnotatedSelectorTest {
    @Flag
    @Resource
    private String annotatedField;
    private String notAnnotatedField;
  }

  @Test
  public void testNoSelector() {
    assertResult(new SelectionTestAdapter<FieldSelector, Field>(){
      public FieldSelector createSelector() {
        return reflect().field("annotatedField");
      }
    }, AnnotatedSelectorTest.class);
    assertResult(new SelectionTestAdapter<FieldSelector, Field>(){
      public FieldSelector createSelector() {
        return reflect().field("notAnnotatedField");
      }
    }, AnnotatedSelectorTest.class);
    assertNoResult(new SelectionTestAdapter<FieldSelector, Field>(){
      public FieldSelector createSelector() {
        return reflect().field("myField");
      }
    }, AnnotatedSelectorTest.class);
  }

  @Test
  public void testAnnotatedSelector() {
    assertResult(new SelectionTestAdapter<FieldSelector, Field>(){
      public FieldSelector createSelector() {
        return reflect().field("annotatedField");
      }
      public void makeSelections(FieldSelector selector) {
        selector.annotated();
      }
      public void assertions(Field field) {
        assertTrue(field.isAnnotationPresent(Flag.class));
      }
    }, AnnotatedSelectorTest.class);
    assertNoResult(new SelectionTestAdapter<FieldSelector, Field>(){
      public FieldSelector createSelector() {
        return reflect().field("notAnnotatedField");
      }
      public void makeSelections(FieldSelector selector) {
        selector.annotated();
      }
    }, AnnotatedSelectorTest.class);
  }

  @Test
  public void testNotAnnotatedSelector() {
    assertResult(new SelectionTestAdapter<FieldSelector, Field>(){
      public FieldSelector createSelector() {
        return reflect().field("notAnnotatedField");
      }
      public void makeSelections(FieldSelector selector) {
        selector.notAnnotated();
      }
      public void assertions(Field field) {
        assertFalse(field.isAnnotationPresent(Flag.class));
      }
    }, AnnotatedSelectorTest.class);
    assertNoResult(new SelectionTestAdapter<FieldSelector, Field>(){
      public FieldSelector createSelector() {
        return reflect().field("annotatedField");
      }
      public void makeSelections(FieldSelector selector) {
        selector.notAnnotated();
      }
    }, AnnotatedSelectorTest.class);
  }

  @Test
  public void testAnnotatedWithSelector() {
    assertResult(new SelectionTestAdapter<FieldSelector, Field>(){
      public FieldSelector createSelector() {
        return reflect().field("annotatedField");
      }
      public void makeSelections(FieldSelector selector) {
        selector.annotatedWith(Flag.class);
      }
      public void assertions(Field field) {
        assertTrue(field.isAnnotationPresent(Flag.class));
      }
    }, AnnotatedSelectorTest.class);
    assertNoResult(new SelectionTestAdapter<FieldSelector, Field>(){
      public FieldSelector createSelector() {
        return reflect().field("notAnnotatedField");
      }
      public void makeSelections(FieldSelector selector) {
        selector.annotatedWith(Flag.class);
      }
    }, AnnotatedSelectorTest.class);
  }

  @Test
  public void testNotAnnotatedWithSelector() {
    assertResult(new SelectionTestAdapter<FieldSelector, Field>(){
      public FieldSelector createSelector() {
        return reflect().field("notAnnotatedField");
      }
      public void makeSelections(FieldSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
      public void assertions(Field field) {
        assertFalse(field.isAnnotationPresent(Flag.class));
      }
    }, AnnotatedSelectorTest.class);
    assertNoResult(new SelectionTestAdapter<FieldSelector, Field>(){
      public FieldSelector createSelector() {
        return reflect().field("annotatedField");
      }
      public void makeSelections(FieldSelector selector) {
        selector.notAnnotatedWith(Flag.class);
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
        return reflect().field("nonFinalField");
      }
      public void makeSelections(FieldSelector selector) {
        selector.nonFinal();
      }
      public void assertions(Field field) {
        assertMatch(field, ReflectionPredicates.NON_FINAL);
      }
    }, NonFinalSelectorTest.class);
    assertNoResult(new SelectionTestAdapter<FieldSelector, Field>(){
      public FieldSelector createSelector() {
        return reflect().field("finalField");
      }
      public void makeSelections(FieldSelector selector) {
        selector.nonFinal();
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
        return reflect().field("nonStaticField");
      }
      public void makeSelections(FieldSelector selector) {
        selector.nonStatic();
      }
      public void assertions(Field field) {
        assertMatch(field, ReflectionPredicates.NON_STATIC);
      }
    }, NonStaticSelectorTest.class);
    assertNoResult(new SelectionTestAdapter<FieldSelector, Field>(){
      public FieldSelector createSelector() {
        return reflect().field("staticField");
      }
      public void makeSelections(FieldSelector selector) {
        selector.nonStatic();
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
    assertNull(reflect().field("i").in(RecursivelySelectorTest.class));
    assertNotNull(reflect().field("i").recursively().in(RecursivelySelectorTest.class));
  }

  @Test
  public void testPredicateSelector() {
    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("i");
      }
      public void makeSelections(FieldSelector selector) {
        selector.thatMatches(Predicates.ALWAYS_TRUE);
      }
    }, BaseClassTest.class);
    assertNoResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("i");
      }
      public void makeSelections(FieldSelector selector) {
        selector.thatMatches(Predicates.ALWAYS_FALSE);
      }
    }, BaseClassTest.class);
  }

  static class AccessSelectorTest {
    private int privateField;
    protected int protectedField;
    int defaultField;
    public int publicField;
  }

  @Test
  public void testAccessSelector() {
    assertAccessSelector(Access.PRIVATE, AccessSelectorTest.class, new SelectionTestAdapter() {
      public Object createSelector() {
        return reflect().field("privateField");
      }
    });
    assertAccessSelector(Access.DEFAULT, AccessSelectorTest.class, new SelectionTestAdapter() {
      public Object createSelector() {
        return reflect().field("defaultField");
      }
    });
    assertAccessSelector(Access.PROTECTED, AccessSelectorTest.class, new SelectionTestAdapter() {
      public Object createSelector() {
        return reflect().field("protectedField");
      }
    });
    assertAccessSelector(Access.PUBLIC, AccessSelectorTest.class, new SelectionTestAdapter() {
      public Object createSelector() {
        return reflect().field("publicField");
      }
    });
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
        return reflect().field("i");
      }
      public void makeSelections(FieldSelector selector) {
        selector.ofType(int.class);
      }
    }, TypeSelectorTest.class);
    assertNoResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("i");
      }
      public void makeSelections(FieldSelector selector) {
        selector.ofType(Integer.class);
      }
    }, TypeSelectorTest.class);

    assertNoResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("I");
      }
      public void makeSelections(FieldSelector selector) {
        selector.ofType(int.class);
      }
    }, TypeSelectorTest.class);
    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("I");
      }
      public void makeSelections(FieldSelector selector) {
        selector.ofType(Integer.class);
      }
    }, TypeSelectorTest.class);

    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("d");
      }
      public void makeSelections(FieldSelector selector) {
        selector.ofType(double.class);
      }
    }, TypeSelectorTest.class);
    assertNoResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("d");
      }
      public void makeSelections(FieldSelector selector) {
        selector.ofType(Double.class);
      }
    }, TypeSelectorTest.class);

    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("D");
      }
      public void makeSelections(FieldSelector selector) {
        selector.ofType(Double.class);
      }
    }, TypeSelectorTest.class);
    assertNoResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("D");
      }
      public void makeSelections(FieldSelector selector) {
        selector.ofType(double.class);
      }
    }, TypeSelectorTest.class);

    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("s");
      }
      public void makeSelections(FieldSelector selector) {
        selector.ofType(String.class);
      }
    }, TypeSelectorTest.class);
    assertNoResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("s");
      }
      public void makeSelections(FieldSelector selector) {
        selector.ofType(Object.class);
      }
    }, TypeSelectorTest.class);
  }

  @Test
  public void testAssignableToSelector() {
    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("i");
      }
      public void makeSelections(FieldSelector selector) {
        selector.assignableTo(int.class);
      }
    }, TypeSelectorTest.class);
    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("i");
      }
      public void makeSelections(FieldSelector selector) {
        selector.assignableTo(Integer.class);
      }
    }, TypeSelectorTest.class);

    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("I");
      }
      public void makeSelections(FieldSelector selector) {
        selector.assignableTo(int.class);
      }
    }, TypeSelectorTest.class);
    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("I");
      }
      public void makeSelections(FieldSelector selector) {
        selector.assignableTo(Integer.class);
      }
    }, TypeSelectorTest.class);

    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("d");
      }
      public void makeSelections(FieldSelector selector) {
        selector.assignableTo(double.class);
      }
    }, TypeSelectorTest.class);
    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("d");
      }
      public void makeSelections(FieldSelector selector) {
        selector.assignableTo(Double.class);
      }
    }, TypeSelectorTest.class);

    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("D");
      }
      public void makeSelections(FieldSelector selector) {
        selector.assignableTo(Double.class);
      }
    }, TypeSelectorTest.class);
    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("D");
      }
      public void makeSelections(FieldSelector selector) {
        selector.assignableTo(double.class);
      }
    }, TypeSelectorTest.class);

    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("s");
      }
      public void makeSelections(FieldSelector selector) {
        selector.assignableTo(String.class);
      }
    }, TypeSelectorTest.class);
    assertResult(new SelectionTestAdapter<FieldSelector, Field>() {
      public FieldSelector createSelector() {
        return reflect().field("s");
      }
      public void makeSelections(FieldSelector selector) {
        selector.assignableTo(Object.class);
      }
    }, TypeSelectorTest.class);
  }

  static class PrecedenceTest extends BaseClassTest {
    int i;
  }

  @Test
  public void testPrecedence() {
    FieldSelector reflector = reflect().field("i");
    Field field1 = reflector.in(BaseClassTest.class);
    Field field2 = reflector.recursively().in(PrecedenceTest.class);

    assertNotNull(field1);
    assertNotNull(field2);
    assertFalse(field1.equals(field2));
  }

}
