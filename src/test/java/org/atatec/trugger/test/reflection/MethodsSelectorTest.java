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
import static org.atatec.trugger.reflection.ReflectionPredicates.ANNOTATED;
import static org.atatec.trugger.reflection.ReflectionPredicates.NON_FINAL;
import static org.atatec.trugger.reflection.ReflectionPredicates.NON_STATIC;
import static org.atatec.trugger.reflection.ReflectionPredicates.NOT_ANNOTATED;
import static org.atatec.trugger.reflection.ReflectionPredicates.annotatedWith;
import static org.atatec.trugger.reflection.ReflectionPredicates.named;
import static org.atatec.trugger.reflection.ReflectionPredicates.notAnnotatedWith;
import static org.atatec.trugger.reflection.ReflectionPredicates.ofReturnType;
import static org.atatec.trugger.reflection.ReflectionPredicates.withParameters;
import static org.atatec.trugger.test.TruggerTest.assertAccessSelector;
import static org.atatec.trugger.test.TruggerTest.assertMatch;
import static org.atatec.trugger.test.TruggerTest.assertNoResult;
import static org.atatec.trugger.test.TruggerTest.assertResult;

import java.lang.reflect.Method;
import java.util.Set;

import org.atatec.trugger.predicate.Predicates;
import org.atatec.trugger.reflection.Access;
import org.atatec.trugger.selector.MethodsSelector;
import org.atatec.trugger.test.Flag;
import org.atatec.trugger.test.SelectionTest;
import org.atatec.trugger.test.SelectionTestAdapter;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class MethodsSelectorTest {

  static class AnnotatedSelectorTest {
    @Flag
    void foo(){};
    void bar(){};
  }

  @Test
  public void testNoSelector() throws Exception {
    assertResult(new SelectionTestAdapter<MethodsSelector, Set<Method>>(){
      public MethodsSelector createSelector() {
        return reflect().methods();
      }
    }, this, 12);
  }

  @Test
  public void testAnnotatedSelector() {
    assertResult(new SelectionTestAdapter<MethodsSelector, Set<Method>>(){
      public MethodsSelector createSelector() {
        return reflect().methods();
      }
      public void makeSelections(MethodsSelector selector) {
        selector.annotated();
      }
      public void assertions(Set<Method> methods) {
        assertMatch(methods, ANNOTATED);
      }
    }, AnnotatedSelectorTest.class, 1);
  }

  @Test
  public void testNotAnnotatedSelector() {
    assertResult(new SelectionTestAdapter<MethodsSelector, Set<Method>>(){
      public MethodsSelector createSelector() {
        return reflect().methods();
      }
      public void makeSelections(MethodsSelector selector) {
        selector.notAnnotated();
      }
      public void assertions(Set<Method> methods) {
        assertMatch(methods, NOT_ANNOTATED);
      }
    }, AnnotatedSelectorTest.class, 1);
  }

  @Test
  public void testAnnotatedWithSelector() {
    assertResult(new SelectionTestAdapter<MethodsSelector, Set<Method>>(){
      public MethodsSelector createSelector() {
        return reflect().methods();
      }
      public void makeSelections(MethodsSelector selector) {
        selector.annotatedWith(Flag.class);
      }
      public void assertions(Set<Method> methods) {
        assertMatch(methods, annotatedWith(Flag.class));
      }
    }, AnnotatedSelectorTest.class, 1);
  }

  @Test
  public void testNotAnnotatedWithSelector() {
    assertResult(new SelectionTestAdapter<MethodsSelector, Set<Method>>(){
      public MethodsSelector createSelector() {
        return reflect().methods();
      }
      public void makeSelections(MethodsSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
      public void assertions(Set<Method> methods) {
        assertMatch(methods, notAnnotatedWith(Flag.class));
      }
    }, AnnotatedSelectorTest.class, 1);
  }

  @Test
  public void testNonFinalSelector() {
    Object obj = new Object(){
      final void foo(){}
      void bar(){}
    };
    assertResult(new SelectionTestAdapter<MethodsSelector, Set<Method>>(){
      public MethodsSelector createSelector() {
        return reflect().methods();
      }
      public void makeSelections(MethodsSelector selector) {
        selector.nonFinal();
      }
      public void assertions(Set<Method> methods) {
        assertMatch(methods, NON_FINAL);
      }
    }, obj, 1);
  }

  static class NonStaticSelector {
    static void foo(){}
    void bar(){}
  }

  @Test
  public void testNonStaticSelector() {
    assertResult(new SelectionTestAdapter<MethodsSelector, Set<Method>>(){
      public MethodsSelector createSelector() {
        return reflect().methods();
      }
      public void makeSelections(MethodsSelector selector) {
        selector.nonStatic();
      }
      public void assertions(Set<Method> methods) {
        assertMatch(methods, NON_STATIC);
      }
    }, NonStaticSelector.class, 1);
  }

  @Test
  public void testRecursivelySelector() {
    Object obj = new Object(){};
    assertNoResult(reflect().methods().in(obj));
    assertResult(reflect().methods().recursively().in(obj));
  }

  @Test
  public void testReturnTypeSelector() {
    Object obj = new Object(){
      void foo(){}
      int bar(){return 0;}
    };
    assertResult(new SelectionTestAdapter<MethodsSelector, Set<Method>>(){
      public MethodsSelector createSelector() {
        return reflect().methods();
      }
      public void makeSelections(MethodsSelector selector) {
        selector.withoutReturnType();
      }
      public void assertions(Set<Method> methods) {
        assertMatch(methods, ofReturnType(Void.TYPE));
      }
    }, obj, 1);
    assertResult(new SelectionTestAdapter<MethodsSelector, Set<Method>>(){
      public MethodsSelector createSelector() {
        return reflect().methods();
      }
      public void makeSelections(MethodsSelector selector) {
        selector.returning(int.class);
      }
      public void assertions(Set<Method> methods) {
        assertMatch(methods, ofReturnType(int.class));
      }
    }, obj, 1);
  }

  @Test
  public void testPredicateSelector() {
    assertResult(new SelectionTestAdapter<MethodsSelector, Set<Method>>(){
      public MethodsSelector createSelector() {
        return reflect().methods();
      }
      public void makeSelections(MethodsSelector selector) {
        selector.that(Predicates.ALWAYS_TRUE);
      }
    }, Object.class);
    assertNoResult(new SelectionTestAdapter<MethodsSelector, Set<Method>>(){
      public MethodsSelector createSelector() {
        return reflect().methods();
      }
      public void makeSelections(MethodsSelector selector) {
        selector.that(Predicates.ALWAYS_FALSE);
      }
    }, Object.class);
  }

  @Test
  public void testAccessSelector() {
    Object obj1 = new Object() {
      private void privateMethod() {}
    };
    Object obj2 = new Object() {
      void defaultMethod() {}
    };
    Object obj3 = new Object() {
      protected void protectedMethod() {}
    };
    Object obj4 = new Object() {
      public void publicMethod() {}
    };
    SelectionTest test = new SelectionTest() {
      public void assertions(Object object) {}
      public void makeSelections(Object selector) {}
      public Object createSelector() {
        return reflect().methods();
      }
    };
    assertAccessSelector(Access.PUBLIC, obj4, test);
    assertAccessSelector(Access.PROTECTED, obj3, test);
    assertAccessSelector(Access.DEFAULT, obj2, test);
    assertAccessSelector(Access.PRIVATE, obj1, test);
  }

  @Test
  public void testParameterSelector() throws Exception {
    Object obj = new Object(){
      void foo(boolean b){}
      void foo2(Boolean b){}
      void bar(boolean b, boolean bool){}
    };
    assertResult(new SelectionTestAdapter<MethodsSelector, Set<Method>>(){
      public MethodsSelector createSelector() {
        return reflect().methods();
      }
      public void makeSelections(MethodsSelector selector) {
        selector.withParameters(boolean.class);
      }
      public void assertions(Set<Method> methods) {
        assertMatch(methods, withParameters(boolean.class).and(named("foo")));
      }
    }, obj, 1);
    assertResult(new SelectionTestAdapter<MethodsSelector, Set<Method>>(){
      public MethodsSelector createSelector() {
        return reflect().methods();
      }
      public void makeSelections(MethodsSelector selector) {
        selector.withParameters(Boolean.class);
      }
      public void assertions(Set<Method> methods) {
        assertMatch(methods, withParameters(Boolean.class).and(named("foo2")));
      }
    }, obj, 1);
    assertResult(new SelectionTestAdapter<MethodsSelector, Set<Method>>(){
      public MethodsSelector createSelector() {
        return reflect().methods();
      }
      public void makeSelections(MethodsSelector selector) {
        selector.withParameters(boolean.class, boolean.class);
      }
      public void assertions(Set<Method> methods) {
        assertMatch(methods, withParameters(boolean.class, boolean.class).and(named("bar")));
      }
    }, obj, 1);
  }

}
