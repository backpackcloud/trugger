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

import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.ElementCopier;
import org.atatec.trugger.element.ElementCopy;
import org.atatec.trugger.element.Elements;
import org.atatec.trugger.predicate.BasePredicate;
import org.atatec.trugger.predicate.CompositePredicate;
import org.atatec.trugger.predicate.Predicate;
import org.atatec.trugger.predicate.PredicateBuilder;
import org.atatec.trugger.selector.ElementsSelector;
import org.atatec.trugger.transformer.Transformer;
import org.atatec.trugger.util.Utils;

import java.util.Set;

/**
 * The default implementation for the property copy operation.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public final class TruggerElementCopier implements ElementCopier {

  private final PredicateBuilder<ElementCopy> builder;

  private ElementsSelector selector;
  private Transformer<Object, ElementCopy> transformer;

  private Object src;
  private Object dest;

  /**
   * @param dest
   *          the receiver object.
   */
  public TruggerElementCopier(Object dest) {
    this.dest = dest;
    this.builder = new PredicateBuilder<ElementCopy>();
  }

  public ElementCopier inSelection(ElementsSelector selector) {
    this.selector = selector.readable();
    return this;
  }

  public ElementCopier notNull() {
    this.builder.add(new Predicate<ElementCopy>() {

      public boolean evaluate(ElementCopy element) {
        return element.value() != null;
      }
    });
    return this;
  }

  public ElementCopier elementsMatching(Predicate<ElementCopy> predicate) {
    this.builder.add(predicate);
    return this;
  }

  public void from(Object src) {
    this.src = src;
    startCopy();
  }

  @Override
  public ElementCopier transformingWith(Transformer transformer) {
    this.transformer = transformer;
    return this;
  }

  private void startCopy() {
    Set<Element> elements;
    if (selector == null) {
      elements = Elements.elements().readable().in(src);
    } else {
      elements = selector.in(src);
    }
    boolean transform = (transformer != null);
    Element destProperty;
    for (Element element : elements) {
      String name = element.name();
      if (src.getClass().equals(dest.getClass())) {
        destProperty = element.isWritable() ? element : null;
      } else {
        destProperty = Elements.element(name).writable().in(dest);
      }
      if (destProperty != null) {
        copy(transform, destProperty, element);
      }
    }
  }

  private void copy(boolean transform, Element destProperty, Element srcProperty) {
    Object value = srcProperty.in(src).value();
    PropertyCopyImpl copy = new PropertyCopyImpl(srcProperty, destProperty, value);
    CompositePredicate<ElementCopy> predicate = builder.predicate();
    if (transform) {
      value = transformer.transform(copy);
    }
    if (value != null) {
      predicate = predicate != null ? predicate.and(new AssignablePredicate(value)) :
        new AssignablePredicate(value);
    }
    if (predicate != null && !predicate.evaluate(copy)) {
      return;
    }
    destProperty.in(dest).value(value);
  }

  private static class PropertyCopyImpl implements ElementCopy {

    private final Element from;

    private final Element to;

    private final Object value;

    private PropertyCopyImpl(Element from, Element to, Object value) {
      this.from = from;
      this.to = to;
      this.value = value;
    }

    public Element sourceElement() {
      return from;
    }

    public Object value() {
      return value;
    }

    public Element destinationElement() {
      return to;
    }

  }

  private static class AssignablePredicate extends BasePredicate<ElementCopy> {

    private final Object value;

    private AssignablePredicate(Object value) {
      this.value = value;
    }

    @Override
    public boolean evaluate(ElementCopy element) {
      return value == null || Utils.areAssignable(element.destinationElement().type(), value.getClass());
    }

  }

}
