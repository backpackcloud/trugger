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

import static org.atatec.trugger.reflection.Access.DEFAULT;
import static org.atatec.trugger.reflection.Access.LIKE_DEFAULT;
import static org.atatec.trugger.reflection.Access.LIKE_PROTECTED;
import static org.atatec.trugger.reflection.Access.PRIVATE;
import static org.atatec.trugger.reflection.Access.PROTECTED;
import static org.atatec.trugger.reflection.Access.PUBLIC;
import static org.atatec.trugger.reflection.Reflection.reflect;
import static org.atatec.trugger.test.TruggerTest.assertMatch;
import static org.atatec.trugger.test.TruggerTest.assertNoResult;
import static org.atatec.trugger.test.TruggerTest.assertResult;

import java.lang.reflect.Constructor;
import java.util.Set;

import org.atatec.trugger.predicate.Predicates;
import org.atatec.trugger.reflection.ReflectionPredicates;
import org.atatec.trugger.selector.ConstructorsSelector;
import org.atatec.trugger.test.Flag;
import org.atatec.trugger.test.SelectionTestAdapter;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ConstructorsSelectorTest {

  static class AnnotatedSelectorTest {
    @Flag
    AnnotatedSelectorTest() {}
    AnnotatedSelectorTest(int i) {}
  }

  @Test
  public void testNoSelector() {
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>(){
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }
    }, AnnotatedSelectorTest.class, 2);
  }

  @Test
  public void testAnnoatedSelector() {
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>(){
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }
      public void makeSelections(ConstructorsSelector selector) {
        selector.annotated();
      }
      public void assertions(Set<Constructor> set) {
        assertMatch(set, ReflectionPredicates.ANNOTATED);
      }
    }, AnnotatedSelectorTest.class, 1);
  }

  @Test
  public void testNotAnnoatedSelector() {
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>(){
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }
      public void makeSelections(ConstructorsSelector selector) {
        selector.notAnnotated();
      }
      public void assertions(Set<Constructor> set) {
        assertMatch(set, ReflectionPredicates.NOT_ANNOTATED);
      }
    }, AnnotatedSelectorTest.class, 1);
  }

  @Test
  public void testAnnoatedWithSelector() {
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>(){
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }
      public void makeSelections(ConstructorsSelector selector) {
        selector.annotatedWith(Flag.class);
      }
      public void assertions(Set<Constructor> set) {
        assertMatch(set, ReflectionPredicates.annotatedWith(Flag.class));
      }
    }, AnnotatedSelectorTest.class, 1);
  }

  @Test
  public void testNotAnnoatedWithSelector() {
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>(){
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }
      public void makeSelections(ConstructorsSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
      public void assertions(Set<Constructor> set) {
        assertMatch(set, ReflectionPredicates.notAnnotatedWith(Flag.class));
      }
    }, AnnotatedSelectorTest.class, 1);
  }

  @Test
  public void testPredicateSelector() {
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>(){
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }
      public void makeSelections(ConstructorsSelector selector) {
        selector.that(Predicates.ALWAYS_TRUE);
      }
    }, AnnotatedSelectorTest.class, 2);

    assertNoResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>(){
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }
      public void makeSelections(ConstructorsSelector selector) {
        selector.that(Predicates.ALWAYS_FALSE);
      }
    }, AnnotatedSelectorTest.class);
  }

  static class AccessSelectorTest {
    public AccessSelectorTest() {}
    private AccessSelectorTest(int i) {}
    AccessSelectorTest(String s) {}
    protected AccessSelectorTest(double d) {}
  }

  @Test
  public void testAccessSelector() {
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>(){
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }
      public void makeSelections(ConstructorsSelector selector) {
        selector.withAccess(PUBLIC);
      }
    }, AccessSelectorTest.class, 1);
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>(){
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }
      public void makeSelections(ConstructorsSelector selector) {
        selector.withAccess(PROTECTED);
      }
    }, AccessSelectorTest.class, 1);
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>(){
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }
      public void makeSelections(ConstructorsSelector selector) {
        selector.withAccess(DEFAULT);
      }
    }, AccessSelectorTest.class, 1);
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>(){
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }
      public void makeSelections(ConstructorsSelector selector) {
        selector.withAccess(PRIVATE);
      }
    }, AccessSelectorTest.class, 1);
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>(){
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }
      public void makeSelections(ConstructorsSelector selector) {
        selector.withAccess(LIKE_DEFAULT);
      }
    }, AccessSelectorTest.class, 3);
    assertResult(new SelectionTestAdapter<ConstructorsSelector, Set<Constructor>>(){
      public ConstructorsSelector createSelector() {
        return reflect().constructors();
      }
      public void makeSelections(ConstructorsSelector selector) {
        selector.withAccess(LIKE_PROTECTED);
      }
    }, AccessSelectorTest.class, 2);
  }

}