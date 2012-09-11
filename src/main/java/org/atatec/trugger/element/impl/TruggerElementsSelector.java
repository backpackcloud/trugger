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
package org.atatec.trugger.element.impl;

import org.atatec.trugger.Finder;
import org.atatec.trugger.Result;
import org.atatec.trugger.bind.BindableElement;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.ElementPredicates;
import org.atatec.trugger.iteration.Iteration;
import org.atatec.trugger.predicate.CompositePredicate;
import org.atatec.trugger.predicate.Predicate;
import org.atatec.trugger.predicate.PredicateBuilder;
import org.atatec.trugger.predicate.Predicates;
import org.atatec.trugger.reflection.ReflectionPredicates;
import org.atatec.trugger.selector.ElementsSelector;
import org.atatec.trugger.transformer.Transformer;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * A default implementation for {@link ElementsSelector}.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public final class TruggerElementsSelector implements ElementsSelector {
  
  private final PredicateBuilder<Element> builder;
  private final Finder<Element> finder;
  
  public TruggerElementsSelector(Finder<Element> finder) {
    this.builder = new PredicateBuilder<Element>(null);
    this.finder = finder;
  }
  
  public ElementsSelector annotatedWith(Class<? extends Annotation> type) {
    builder.add(ReflectionPredicates.isAnnotatedWith(type));
    return this;
  }
  
  public ElementsSelector notAnnotatedWith(Class<? extends Annotation> type) {
    builder.add(ReflectionPredicates.isNotAnnotatedWith(type));
    return this;
  }
  
  public ElementsSelector annotated() {
    builder.add(ReflectionPredicates.IS_ANNOTATED);
    return this;
  }
  
  public ElementsSelector notAnnotated() {
    builder.add(ReflectionPredicates.IS_NOT_ANNOTATED);
    return this;
  }
  
  public ElementsSelector ofType(Class<?> type) {
    builder.add(ElementPredicates.ofType(type));
    return this;
  }
  
  public ElementsSelector assignableTo(Class<?> type) {
    builder.add(ElementPredicates.assignableTo(type));
    return this;
  }
  
  @Override
  public ElementsSelector nonSpecific() {
    builder.add(ElementPredicates.NON_SPECIFIC);
    return this;
  }
  
  @Override
  public ElementsSelector specific() {
    builder.add(ElementPredicates.SPECIFIC);
    return this;
  }
  
  public ElementsSelector named(String... names) {
    builder.add(ElementPredicates.named(names));
    return this;
  }
  
  public ElementsSelector that(final Predicate<? super Element> predicate) {
    builder.add(predicate);
    return this;
  }
  
  public ElementsSelector nonReadable() {
    builder.add(ElementPredicates.NON_READABLE);
    return this;
  }
  
  public ElementsSelector nonWritable() {
    builder.add(ElementPredicates.NON_WRITABLE);
    return this;
  }
  
  public ElementsSelector readable() {
    builder.add(ElementPredicates.READABLE);
    return this;
  }
  
  public ElementsSelector writable() {
    builder.add(ElementPredicates.WRITABLE);
    return this;
  }
  
  public CompositePredicate<Element> toPredicate() {
    CompositePredicate<Element> predicate = builder.predicate();
    return predicate != null ? predicate : Predicates.ALWAYS_TRUE;
  }
  
  public Set<Element> in(Object target) {
    Set<Element> set = finder.findAll().in(target);
    Predicate<Element> predicate = builder.predicate();
    if (predicate != null) {
      Iteration.retainFrom(set).elements(predicate);
    }
    return set;
  }
  
  public Result<Set<BindableElement>, Object> forBind() {
    writable();
    return new Result<Set<BindableElement>, Object>() {
      
      public Set<BindableElement> in(Object target) {
        Set<Element> set = TruggerElementsSelector.this.in(target);
        Set<BindableElement> result = new HashSet<BindableElement>(set.size());
        Transformer<BindableElement, Element> t = new ElementToBindableTransformer(target);
        Iteration.copyTo(result).transformingWith(t).all().from(set);
        return result;
      }
    };
  }
  
  private static class ElementToBindableTransformer implements Transformer<BindableElement, Element> {
    
    private final Object target;
    
    public ElementToBindableTransformer(Object target) {
      this.target = target;
    }
    
    public BindableElement transform(Element object) {
      return new TruggerBindableElement(object, target);
    }
    
  }
  
}
