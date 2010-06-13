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

import java.util.Set;

import net.sf.trugger.element.Element;
import net.sf.trugger.element.ElementCopier;
import net.sf.trugger.element.ElementCopy;
import net.sf.trugger.element.Elements;
import net.sf.trugger.predicate.CompositePredicate;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.predicate.PredicateBuilder;
import net.sf.trugger.selector.ElementsSelector;
import net.sf.trugger.transformer.Transformer;
import net.sf.trugger.util.Utils;

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
      predicate = predicate.and(new AssignablePredicate(value));
    }
    if (predicate.evaluate(copy)) {
      destProperty.in(dest).value(value);
    }
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

  private static class AssignablePredicate implements Predicate<ElementCopy> {

    private final Object value;

    private AssignablePredicate(Object value) {
      this.value = value;
    }

    @Override
    public boolean evaluate(ElementCopy element) {
      if (value == null) {
        return true;
      }
      return Utils.areAssignable(element.destinationElement().type(), value.getClass());
    }

  }

}
