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
import static org.atatec.trugger.test.TruggerTest.assertNoResult;
import static org.atatec.trugger.test.TruggerTest.assertResult;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.atatec.trugger.predicate.Predicates;
import org.atatec.trugger.selector.FieldGetterMethodSelector;
import org.atatec.trugger.test.Flag;
import org.atatec.trugger.test.SelectionTestAdapter;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class FieldGetterMethodSelectorTest {

  private int count;
  @Flag
  private int hits;

  @Flag
  @Resource
  public int getCount() {
    return count;
  }

  public int getHits() {
    return hits;
  }

  private Field fieldCount;
  private Field fieldHits;

  @Before
  public void initialize() {
    fieldCount = reflect().field("count").in(this);
    fieldHits = reflect().field("hits").in(this);
  }

  @Test
  public void testNoSelector() {
    assertResult(new SelectionTestAdapter<FieldGetterMethodSelector, Method>(){
      public FieldGetterMethodSelector createSelector() {
        return reflect().getterOf(fieldCount);
      }
    }, this);
    assertResult(new SelectionTestAdapter<FieldGetterMethodSelector, Method>(){
      public FieldGetterMethodSelector createSelector() {
        return reflect().getterOf(fieldHits);
      }
    }, this);
    assertNoResult(new SelectionTestAdapter<FieldGetterMethodSelector, Method>(){
      public FieldGetterMethodSelector createSelector() {
        return reflect().getterOf(fieldCount);
      }
    }, Object.class);
  }

  @Test
  public void testAnnotatedSelector() {
    assertResult(new SelectionTestAdapter<FieldGetterMethodSelector, Method>(){
      public FieldGetterMethodSelector createSelector() {
        return reflect().getterOf(fieldCount);
      }
      public void makeSelections(FieldGetterMethodSelector selector) {
        selector.annotated();
      }
      public void assertions(Method method) {
        assertTrue(method.getDeclaredAnnotations().length > 0);
      }
    }, this);
    assertNoResult(new SelectionTestAdapter<FieldGetterMethodSelector, Method>(){
      public FieldGetterMethodSelector createSelector() {
        return reflect().getterOf(fieldHits);
      }
      public void makeSelections(FieldGetterMethodSelector selector) {
        selector.annotated();
      }
    }, this);
  }

  @Test
  public void testNotAnnotatedSelector() {
    assertResult(new SelectionTestAdapter<FieldGetterMethodSelector, Method>(){
      public FieldGetterMethodSelector createSelector() {
        return reflect().getterOf(fieldHits);
      }
      public void makeSelections(FieldGetterMethodSelector selector) {
        selector.notAnnotated();
      }
      public void assertions(Method method) {
        assertTrue(method.getDeclaredAnnotations().length == 0);
      }
    }, this);
    assertNoResult(new SelectionTestAdapter<FieldGetterMethodSelector, Method>(){
      public FieldGetterMethodSelector createSelector() {
        return reflect().getterOf(fieldCount);
      }
      public void makeSelections(FieldGetterMethodSelector selector) {
        selector.notAnnotated();
      }
    }, this);
  }

  @Test
  public void testAnnotatedWithSelector() {
    assertResult(new SelectionTestAdapter<FieldGetterMethodSelector, Method>(){
      public FieldGetterMethodSelector createSelector() {
        return reflect().getterOf(fieldCount);
      }
      public void makeSelections(FieldGetterMethodSelector selector) {
        selector.annotatedWith(Flag.class);
      }
      public void assertions(Method method) {
        assertTrue(method.isAnnotationPresent(Flag.class));
      }
    }, this);
    assertNoResult(new SelectionTestAdapter<FieldGetterMethodSelector, Method>(){
      public FieldGetterMethodSelector createSelector() {
        return reflect().getterOf(fieldHits);
      }
      public void makeSelections(FieldGetterMethodSelector selector) {
        selector.annotatedWith(Flag.class);
      }
    }, this);
  }

  @Test
  public void testNotAnnotatedWithSelector() {
    assertResult(new SelectionTestAdapter<FieldGetterMethodSelector, Method>(){
      public FieldGetterMethodSelector createSelector() {
        return reflect().getterOf(fieldHits);
      }
      public void makeSelections(FieldGetterMethodSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
      public void assertions(Method method) {
        assertTrue(method.getDeclaredAnnotations().length == 0);
      }
    }, this);
    assertNoResult(new SelectionTestAdapter<FieldGetterMethodSelector, Method>(){
      public FieldGetterMethodSelector createSelector() {
        return reflect().getterOf(fieldCount);
      }
      public void makeSelections(FieldGetterMethodSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
    }, this);
  }

  @Test
  public void testPredicateSelector() {
    assertResult(new SelectionTestAdapter<FieldGetterMethodSelector, Method>(){
      public FieldGetterMethodSelector createSelector() {
        return reflect().getterOf(fieldCount);
      }
      public void makeSelections(FieldGetterMethodSelector selector) {
        selector.that(Predicates.ALWAYS_TRUE);
      }
    }, this);
    assertNoResult(new SelectionTestAdapter<FieldGetterMethodSelector, Method>(){
      public FieldGetterMethodSelector createSelector() {
        return reflect().getterOf(fieldCount);
      }
      public void makeSelections(FieldGetterMethodSelector selector) {
        selector.that(Predicates.ALWAYS_FALSE);
      }
    }, this);
  }

}
