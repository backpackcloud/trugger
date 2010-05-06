/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
import java.util.HashSet;
import java.util.Set;

import net.sf.trugger.Finder;
import net.sf.trugger.Result;
import net.sf.trugger.Transformer;
import net.sf.trugger.bind.BindableElement;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.ElementPredicates;
import net.sf.trugger.iteration.Iteration;
import net.sf.trugger.predicate.CompositePredicate;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.predicate.PredicateBuilder;
import net.sf.trugger.predicate.Predicates;
import net.sf.trugger.reflection.ReflectionPredicates;
import net.sf.trugger.selector.ElementsSelector;

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
    builder.add(ReflectionPredicates.annotatedWith(type));
    return this;
  }
  
  public ElementsSelector notAnnotatedWith(Class<? extends Annotation> type) {
    builder.add(ReflectionPredicates.notAnnotatedWith(type));
    return this;
  }
  
  public ElementsSelector annotated() {
    builder.add(ReflectionPredicates.ANNOTATED);
    return this;
  }
  
  public ElementsSelector notAnnotated() {
    builder.add(ReflectionPredicates.NOT_ANNOTATED);
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
  
  public ElementsSelector thatMatches(final Predicate<? super Element> predicate) {
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
      Iteration.retainFrom(set).elementsMatching(predicate);
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
        Iteration.copyTo(result).transformingWith(t).allElements().from(set);
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
