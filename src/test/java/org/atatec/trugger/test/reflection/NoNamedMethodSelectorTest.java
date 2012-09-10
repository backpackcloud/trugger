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
import org.atatec.trugger.reflection.ReflectionException;
import org.atatec.trugger.selector.MethodSelector;
import org.atatec.trugger.test.Flag;
import org.atatec.trugger.test.SelectionTestAdapter;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.atatec.trugger.reflection.Reflection.reflect;
import static org.atatec.trugger.reflection.ReflectionPredicates.ANNOTATED;
import static org.atatec.trugger.reflection.ReflectionPredicates.NOT_ANNOTATED;
import static org.atatec.trugger.reflection.ReflectionPredicates.annotatedWith;
import static org.atatec.trugger.reflection.ReflectionPredicates.dontDeclare;
import static org.atatec.trugger.reflection.ReflectionPredicates.notAnnotatedWith;
import static org.atatec.trugger.test.TruggerTest.assertMatch;
import static org.atatec.trugger.test.TruggerTest.assertNoResult;
import static org.atatec.trugger.test.TruggerTest.assertResult;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class NoNamedMethodSelectorTest {

  static class SimpleObject {
    void foo(){};
  }

  @Test
  public void simpleTest() {
    assertResult(reflect().method().in(SimpleObject.class));
  }

  static class AnnotatedSelectorTest {
    @Flag
    void foo(){};
    void bar(){};
  }

  @Test(expected = ReflectionException.class)
  public void testMultipleMatch() {
    reflect().method().in(AnnotatedSelectorTest.class);
  }

  @Test
  public void testAnnotatedSelector() {
    assertResult(new SelectionTestAdapter<MethodSelector, Method>(){
      public MethodSelector createSelector() {
        return reflect().method();
      }
      public void makeSelections(MethodSelector selector) {
        selector.annotated();
      }
      @Override
      public void assertions(Method method) {
        assertMatch(method, ANNOTATED);
      }
    }, AnnotatedSelectorTest.class);
  }

  @Test
  public void testNotAnnotatedSelector() {
    assertResult(new SelectionTestAdapter<MethodSelector, Method>(){
      public MethodSelector createSelector() {
        return reflect().method();
      }
      public void makeSelections(MethodSelector selector) {
        selector.notAnnotated();
      }
      @Override
      public void assertions(Method method) {
        assertMatch(method, NOT_ANNOTATED);
      }
    }, AnnotatedSelectorTest.class);
  }

  @Test
  public void testAnnotatedWithSelector() {
    assertResult(new SelectionTestAdapter<MethodSelector, Method>(){
      public MethodSelector createSelector() {
        return reflect().method();
      }
      public void makeSelections(MethodSelector selector) {
        selector.annotatedWith(Flag.class);
      }
      @Override
      public void assertions(Method method) {
        assertMatch(method, annotatedWith(Flag.class));
      }
    }, AnnotatedSelectorTest.class);
  }

  @Test
  public void testNotAnnotatedWithSelector() {
    assertResult(new SelectionTestAdapter<MethodSelector, Method>(){
      public MethodSelector createSelector() {
        return reflect().method();
      }
      public void makeSelections(MethodSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
      @Override
      public void assertions(Method method) {
        assertMatch(method, notAnnotatedWith(Flag.class));
      }
    }, AnnotatedSelectorTest.class);
  }

  @Test
  public void testNonFinalSelector() {
    Object obj = new Object(){
      void foo(){}
      final void bar(){}
    };
    assertResult(new SelectionTestAdapter<MethodSelector, Method>(){
      public MethodSelector createSelector() {
        return reflect().method();
      }
      public void makeSelections(MethodSelector selector) {
        selector.nonFinal();
      }
      public void assertions(Method method) {
        assertMatch(method, dontDeclare(Modifier.FINAL));
      }
    }, obj);
  }

  static class NonStaticSelector {
    void foo(){}
    static void bar(){}
  }

  @Test
  public void testNonStaticSelector() {
    assertResult(new SelectionTestAdapter<MethodSelector, Method>(){
      public MethodSelector createSelector() {
        return reflect().method();
      }
      public void makeSelections(MethodSelector selector) {
        selector.nonStatic();
      }
      public void assertions(Method method) {
        assertMatch(method, dontDeclare(Modifier.STATIC));
      }
    }, NonStaticSelector.class);
  }

  static class RecursionTestObject {
    @Flag
    void foo(){};
  }

  @Test
  public void testRecursivelySelector() {
    assertNull(reflect().method().annotated().in(new RecursionTestObject(){}));
    assertNotNull(reflect().method().annotated().recursively().in(new RecursionTestObject(){}));
  }

  @Test
  public void testReturnTypeSelector() {
    Object obj = new Object(){
      void foo(){}
      int bar(){return 0;}
    };
    assertResult(new SelectionTestAdapter<MethodSelector, Method>(){
      public MethodSelector createSelector() {
        return reflect().method();
      }
      public void makeSelections(MethodSelector selector) {
        selector.withoutReturnType();
      }
      public void assertions(Method method) {
        assertEquals(Void.TYPE, method.getReturnType());
      }
    }, obj);
    assertResult(new SelectionTestAdapter<MethodSelector, Method>(){
      public MethodSelector createSelector() {
        return reflect().method();
      }
      public void makeSelections(MethodSelector selector) {
        selector.returning(int.class);
      }
      public void assertions(Method method) {
        assertEquals(int.class, method.getReturnType());
      }
    }, obj);
  }

  @Test
  public void testPredicateSelector() {
    assertResult(new SelectionTestAdapter<MethodSelector, Method>(){
      public MethodSelector createSelector() {
        return reflect().method();
      }
      public void makeSelections(MethodSelector selector) {
        selector.that(Predicates.ALWAYS_TRUE);
      }
    }, SimpleObject.class);
    assertNoResult(new SelectionTestAdapter<MethodSelector, Method>(){
      public MethodSelector createSelector() {
        return reflect().method();
      }
      public void makeSelections(MethodSelector selector) {
        selector.that(Predicates.ALWAYS_FALSE);
      }
    }, SimpleObject.class);
  }

  @Test
  public void testParameterSelector() throws Exception {
    Object obj = new Object(){
      void foo(boolean b){}
      void foo2(Boolean b){}
      void bar(boolean b, boolean bool){}
    };
    assertResult(new SelectionTestAdapter<MethodSelector, Method>(){
      public MethodSelector createSelector() {
        return reflect().method();
      }
      public void makeSelections(MethodSelector selector) {
        selector.withParameters(boolean.class);
      }
      public void assertions(Method method) {
        assertArrayEquals(new Class[]{boolean.class}, method.getParameterTypes());
      }
    }, obj);
    assertResult(new SelectionTestAdapter<MethodSelector, Method>(){
      public MethodSelector createSelector() {
        return reflect().method();
      }
      public void makeSelections(MethodSelector selector) {
        selector.withParameters(Boolean.class);
      }
    }, obj);
    assertResult(new SelectionTestAdapter<MethodSelector, Method>(){
      public MethodSelector createSelector() {
        return reflect().method();
      }
      public void makeSelections(MethodSelector selector) {
        selector.withParameters(boolean.class, boolean.class);
      }
      public void assertions(Method method) {
        assertArrayEquals(new Class[]{boolean.class, boolean.class}, method.getParameterTypes());
      }
    }, obj);
  }

}
