/*
 * The Apache License
 *
 * Copyright 2009 Marcelo Guimaraes <ataxexe@backpackcloud.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.backpackcloud.trugger.element.impl;

import io.backpackcloud.trugger.element.CopyDestinationMapper;
import io.backpackcloud.trugger.element.Element;
import io.backpackcloud.trugger.element.ElementCopier;
import io.backpackcloud.trugger.element.ElementCopy;
import io.backpackcloud.trugger.element.Elements;
import io.backpackcloud.trugger.element.ElementsSelector;
import io.backpackcloud.trugger.util.Utils;
import tools.devnull.trugger.element.*;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The default implementation for the property copy operation.
 *
 * @author Marcelo "Ataxexe" Guimarães
 */
public final class TruggerElementCopier implements ElementCopier, CopyDestinationMapper {

  private final ElementsSelector selector;
  private final Function<ElementCopy, Object> function;
  private final Predicate<ElementCopy> predicate;
  private final Object src;

  public TruggerElementCopier() {
    this.selector = Elements.elements();
    this.function = ElementCopy::value;
    this.predicate = copy -> true;
    this.src = null;
  }

  public TruggerElementCopier(ElementsSelector selector) {
    this.selector = selector;
    this.function = ElementCopy::value;
    this.predicate = copy -> true;
    this.src = null;
  }

  private TruggerElementCopier(ElementsSelector selector,
                               Function<ElementCopy, Object> function,
                               Predicate<ElementCopy> predicate,
                               Object src) {
    this.selector = selector;
    this.function = function;
    this.predicate = predicate;
    this.src = src;
  }

  public CopyDestinationMapper notNull() {
    return new TruggerElementCopier(selector, function, predicate.and(o -> o.src().getValue() != null), src);
  }

  public CopyDestinationMapper from(Object src) {
    return new TruggerElementCopier(selector, function, predicate, src);
  }

  @Override
  public CopyDestinationMapper filter(Predicate<ElementCopy> predicate) {
    return new TruggerElementCopier(this.selector, this.function, this.predicate.and(predicate), this.src);
  }

  @Override
  public CopyDestinationMapper map(Function function) {
    return new TruggerElementCopier(selector, function, predicate, src);
  }

  public void to(Object object) {
    startCopy(object);
  }

  private void startCopy(Object dest) {
    List<Element> elements = selector.from(src);
    Element destProperty;
    for (Element element : elements) {
      String name = element.name();
      if (src.getClass().equals(dest.getClass())) {
        destProperty = element.isWritable() ? element : null;
      } else {
        destProperty = Elements.element(name).from(dest).result();
      }
      if (destProperty != null && element.isReadable() && destProperty.isWritable()) {
        copy(destProperty, element, dest);
      }
    }
  }

  private void copy(Element destElement, Element srcElement, Object dest) {
    Object value = srcElement.on(this.src).getValue();
    PropertyCopyImpl copy = new PropertyCopyImpl(srcElement, destElement, value);
    if (predicate.test(copy)) {
      value = function.apply(copy);
      if (value == null || Utils.areAssignable(destElement.type(), value.getClass())) {
        destElement.on(dest).setValue(value);
      }
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

    public Element src() {
      return from;
    }

    public Object value() {
      return value;
    }

    public Element dest() {
      return to;
    }

  }

}
