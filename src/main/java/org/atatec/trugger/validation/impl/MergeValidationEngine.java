/*
 * Copyright 2009-2014 Marcelo Guimarães
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

package org.atatec.trugger.validation.impl;

import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.Elements;
import org.atatec.trugger.validation.InvalidElement;
import org.atatec.trugger.validation.ValidationEngine;
import org.atatec.trugger.validation.ValidationResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A validation engine that merges the invalid elements with the main validation
 * result.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public class MergeValidationEngine implements ValidationEngine {

  private final ValidationEngine engine;
  private final Map<String, InvalidElement> invalidElements;
  private final Element element;
  private final Predicate<Element> filter;

  public MergeValidationEngine(ValidationEngine engine,
                               Map<String, InvalidElement> invalidElements,
                               Element element,
                               Predicate<Element> filter) {
    this.engine = engine;
    this.invalidElements = invalidElements;
    this.element = element;
    this.filter = filter;
  }

  @Override
  public ValidationEngine filter(Predicate<Element> filter) {
    return new MergeValidationEngine(engine, invalidElements, element, filter);
  }

  @Override
  public ValidationResult validate(Object target) {
    if (target instanceof List) {
      ValidationResultImpl result = new ValidationResultImpl(target);
      AtomicInteger index = new AtomicInteger(0);
      for (Object o : (List) target) {
        _validate(o, String.valueOf(index) + ".",
            (el) -> result.invalidElements.put(index.toString(), el));
        index.incrementAndGet();
      }
      /*if (result.isInvalid()) {
        invalidElements.put(element.name(),
            new InvalidElementImpl(element, target, null)
        );
      }*/
      return result;
    }
    return _validate(target, "", (el) -> {
      return;
    });
  }

  private ValidationResult _validate(Object target, String mod,
                                     Consumer<InvalidElement> consumer) {
    String parent = element.name();
    ValidationResult result = engine.filter(filter).validate(target);
    for (InvalidElement invalidElement : result.invalidElements()) {
      consumer.accept(invalidElement);
      String newName = String.format(
          "%s.%s%s", parent, mod, invalidElement.name()
      );
      Element nestedElement = Elements.element(newName).in(target);
      invalidElements.put(newName,
          new InvalidElementImpl(
              nestedElement,
              invalidElement.invalidValue(),
              invalidElement.violatedConstraints())
      );
    }
    return result;
  }

}
