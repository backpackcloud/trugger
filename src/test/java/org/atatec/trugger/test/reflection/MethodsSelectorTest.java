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
import org.atatec.trugger.selector.MethodsSelector;
import org.atatec.trugger.test.Flag;
import org.atatec.trugger.test.SelectionTest;
import org.atatec.trugger.test.SelectionTestAdapter;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

import static org.atatec.trugger.reflection.Reflection.reflect;
import static org.atatec.trugger.reflection.ReflectionPredicates.IS_ANNOTATED;
import static org.atatec.trugger.reflection.ReflectionPredicates.IS_NOT_ANNOTATED;
import static org.atatec.trugger.reflection.ReflectionPredicates.isAnnotatedWith;
import static org.atatec.trugger.reflection.ReflectionPredicates.dontDeclare;
import static org.atatec.trugger.reflection.ReflectionPredicates.named;
import static org.atatec.trugger.reflection.ReflectionPredicates.isNotAnnotatedWith;
import static org.atatec.trugger.reflection.MethodPredicates.returns;
import static org.atatec.trugger.reflection.MethodPredicates.takes;
import static org.atatec.trugger.test.TruggerTest.assertMatch;
import static org.atatec.trugger.test.TruggerTest.assertNoResult;
import static org.atatec.trugger.test.TruggerTest.assertResult;

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
        assertMatch(methods, IS_ANNOTATED);
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
        assertMatch(methods, IS_NOT_ANNOTATED);
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
        assertMatch(methods, isAnnotatedWith(Flag.class));
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
        assertMatch(methods, isNotAnnotatedWith(Flag.class));
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
        assertMatch(methods, dontDeclare(Modifier.FINAL));
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
        assertMatch(methods, dontDeclare(Modifier.STATIC));
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
        assertMatch(methods, returns(Void.TYPE));
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
        assertMatch(methods, returns(int.class));
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
        assertMatch(methods, takes(boolean.class).and(named("foo")));
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
        assertMatch(methods, takes(Boolean.class).and(named("foo2")));
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
        assertMatch(methods, takes(boolean.class, boolean.class).and(named("bar")));
      }
    }, obj, 1);
  }

}
