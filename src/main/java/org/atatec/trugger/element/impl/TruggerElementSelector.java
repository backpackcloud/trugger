/*
 * Copyright 2009-2012 Marcelo Guimarães
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
package org.atatec.trugger.element.impl;

import org.atatec.trugger.Finder;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.ElementPredicates;
import org.atatec.trugger.reflection.ReflectionPredicates;
import org.atatec.trugger.selector.ElementSelector;

import java.lang.annotation.Annotation;
import java.util.function.Predicate;

/**
 * A default implementation for {@link ElementSelector}.
 *
 * @author Marcelo Guimarães
 */
public class TruggerElementSelector implements ElementSelector {

  protected Predicate<Element> predicate;
  private final Finder<Element> finder;
  private final String name;

  /**
   * Creates a new selector for the element with the specified name.
   *
   * @param name
   *          the element name for selection.
   */
  public TruggerElementSelector(String name, Finder<Element> finder) {
    this.name = name;
    this.finder = finder;
  }

  protected void add(Predicate other) {
    if (predicate != null) {
      predicate = predicate.and(other);
    } else {
      predicate = other;
    }
  }

  public ElementSelector annotated() {
    add(ReflectionPredicates.ANNOTATED);
    return this;
  }

  public ElementSelector notAnnotated() {
    add(ReflectionPredicates.NOT_ANNOTATED);
    return this;
  }

  public ElementSelector annotatedWith(Class<? extends Annotation> type) {
    add(ReflectionPredicates.isAnnotatedWith(type));
    return this;
  }

  public ElementSelector notAnnotatedWith(Class<? extends Annotation> type) {
    add(ReflectionPredicates.isNotAnnotatedWith(type));
    return this;
  }

  public ElementSelector ofType(Class<?> type) {
    add(ElementPredicates.ofType(type));
    return this;
  }

  public ElementSelector assignableTo(Class<?> type) {
    add(ElementPredicates.assignableTo(type));
    return this;
  }

  public ElementSelector that(final Predicate<? super Element> predicate) {
    add(predicate);
    return this;
  }

  public ElementSelector nonReadable() {
    add(ElementPredicates.NON_READABLE);
    return this;
  }

  public ElementSelector nonWritable() {
    add(ElementPredicates.NON_WRITABLE);
    return this;
  }

  public ElementSelector readable() {
    add(ElementPredicates.READABLE);
    return this;
  }

  public ElementSelector nonSpecific() {
    add(ElementPredicates.NON_SPECIFIC);
    return this;
  }

  public ElementSelector specific() {
    add(ElementPredicates.SPECIFIC);
    return this;
  }

  public ElementSelector writable() {
    add(ElementPredicates.WRITABLE);
    return this;
  }

  public Element in(Object target) {
    Element element = finder.find(name).in(target);
    if (element == null) {
      return null;
    }
    if(predicate != null) {
      return predicate.test(element) ? element : null;
    }
    return element;
  }

  protected Finder<Element> finder() {
    return finder;
  }

}
