/*
 * Copyright 2009-2011 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.element.impl;

import java.lang.annotation.Annotation;

import net.sf.trugger.Finder;
import net.sf.trugger.Result;
import net.sf.trugger.bind.BindableElement;
import net.sf.trugger.bind.impl.BindableElementTransformer;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.ElementPredicates;
import net.sf.trugger.predicate.CompositePredicate;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.predicate.PredicateBuilder;
import net.sf.trugger.reflection.ReflectionPredicates;
import net.sf.trugger.selector.ElementSelector;

/**
 * A default implementation for {@link ElementSelector}.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerElementSelector implements ElementSelector {

  private final PredicateBuilder<Element> builder;
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
    builder = new PredicateBuilder<Element>();
  }

  public ElementSelector annotated() {
    builder.add(ReflectionPredicates.ANNOTATED);
    return this;
  }

  public ElementSelector notAnnotated() {
    builder.add(ReflectionPredicates.NOT_ANNOTATED);
    return this;
  }

  public ElementSelector annotatedWith(Class<? extends Annotation> type) {
    builder.add(ReflectionPredicates.annotatedWith(type));
    return this;
  }

  public ElementSelector notAnnotatedWith(Class<? extends Annotation> type) {
    builder.add(ReflectionPredicates.notAnnotatedWith(type));
    return this;
  }

  public ElementSelector ofType(Class<?> type) {
    builder.add(ElementPredicates.ofType(type));
    return this;
  }

  public ElementSelector assignableTo(Class<?> type) {
    builder.add(ElementPredicates.assignableTo(type));
    return this;
  }

  public ElementSelector thatMatches(final Predicate<? super Element> predicate) {
    builder.add(predicate);
    return this;
  }

  public ElementSelector nonReadable() {
    builder.add(ElementPredicates.NON_READABLE);
    return this;
  }

  public ElementSelector nonWritable() {
    builder.add(ElementPredicates.NON_WRITABLE);
    return this;
  }

  public ElementSelector readable() {
    builder.add(ElementPredicates.READABLE);
    return this;
  }

  public ElementSelector nonSpecific() {
    builder.add(ElementPredicates.NON_SPECIFIC);
    return this;
  }

  public ElementSelector specific() {
    builder.add(ElementPredicates.SPECIFIC);
    return this;
  }

  @Override
  public CompositePredicate<Element> toPredicate() {
    return builder.predicate();
  }

  public ElementSelector writable() {
    builder.add(ElementPredicates.WRITABLE);
    return this;
  }

  public Element in(Object target) {
    Element element = finder.find(name).in(target);
    if (element == null) {
      return null;
    }
    return builder.predicate().evaluate(element) ? element : null;
  }

  public Result<BindableElement, Object> forBind() {
    writable();
    return new Result<BindableElement, Object>() {

      public BindableElement in(Object target) {
        Element element = TruggerElementSelector.this.in(target);
        return new BindableElementTransformer(target).transform(element);
      }
    };
  }

  protected PredicateBuilder<Element> builder() {
    return builder;
  }

  protected Finder<Element> finder() {
    return finder;
  }

}
