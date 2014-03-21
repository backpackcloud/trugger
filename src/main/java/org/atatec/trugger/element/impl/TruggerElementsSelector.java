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
import org.atatec.trugger.selector.ElementsSelector;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A default implementation for {@link ElementsSelector}.
 *
 * @author Marcelo Guimarães
 */
public final class TruggerElementsSelector implements ElementsSelector {

  private Predicate<? super Element> predicate;
  private final Finder<Element> finder;

  public TruggerElementsSelector(Finder<Element> finder) {
    this.finder = finder;
  }

  private void add(Predicate other) {
    if (predicate != null) {
      predicate = predicate.and(other);
    } else {
      predicate = other;
    }
  }

  public ElementsSelector annotatedWith(Class<? extends Annotation> type) {
    add(ReflectionPredicates.isAnnotatedWith(type));
    return this;
  }

  public ElementsSelector notAnnotatedWith(Class<? extends Annotation> type) {
    add(ReflectionPredicates.isNotAnnotatedWith(type));
    return this;
  }

  public ElementsSelector annotated() {
    add(ReflectionPredicates.ANNOTATED);
    return this;
  }

  public ElementsSelector notAnnotated() {
    add(ReflectionPredicates.NOT_ANNOTATED);
    return this;
  }

  public ElementsSelector ofType(Class<?> type) {
    add(ElementPredicates.ofType(type));
    return this;
  }

  public ElementsSelector assignableTo(Class<?> type) {
    add(ElementPredicates.assignableTo(type));
    return this;
  }

  @Override
  public ElementsSelector nonSpecific() {
    add(ElementPredicates.NON_SPECIFIC);
    return this;
  }

  @Override
  public ElementsSelector specific() {
    add(ElementPredicates.SPECIFIC);
    return this;
  }

  public ElementsSelector named(String... names) {
    add(ElementPredicates.named(names));
    return this;
  }

  public ElementsSelector that(final Predicate<? super Element> predicate) {
    add(predicate);
    return this;
  }

  public ElementsSelector nonReadable() {
    add(ElementPredicates.NON_READABLE);
    return this;
  }

  public ElementsSelector nonWritable() {
    add(ElementPredicates.NON_WRITABLE);
    return this;
  }

  public ElementsSelector readable() {
    add(ElementPredicates.READABLE);
    return this;
  }

  public ElementsSelector writable() {
    add(ElementPredicates.WRITABLE);
    return this;
  }

  public Set<Element> in(Object target) {
    Set<Element> elements = finder.findAll().in(target);
    if (predicate != null) {
      return elements.stream().filter(predicate).collect(Collectors.toSet());
    }
    return elements;
  }

}
