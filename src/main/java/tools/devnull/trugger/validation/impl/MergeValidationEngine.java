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

package tools.devnull.trugger.validation.impl;

import tools.devnull.trugger.element.Element;
import tools.devnull.trugger.element.Elements;
import tools.devnull.trugger.validation.InvalidElement;
import tools.devnull.trugger.validation.ValidationEngine;
import tools.devnull.trugger.validation.ValidationResult;

import java.util.Map;
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
  private final Object mainTarget;

  public MergeValidationEngine(ValidationEngine engine,
                               Map<String, InvalidElement> invalidElements,
                               Element element,
                               Predicate<Element> filter,
                               Object target) {
    this.engine = engine;
    this.invalidElements = invalidElements;
    this.element = element;
    this.filter = filter;
    this.mainTarget = target;
  }

  @Override
  public ValidationEngine filter(Predicate<Element> filter) {
    return new MergeValidationEngine(engine, invalidElements, element, filter, mainTarget);
  }

  @Override
  public ValidationResult validate(Object target) {
    String parent = element.name();
    ValidationResult result = engine.filter(filter).validate(target);
    for (InvalidElement invalidElement : result.invalidElements()) {
      String newName = String.format(
          "%s.%s", parent, invalidElement.name()
      );
      Element nestedElement = Elements.element(newName).in(mainTarget);
      invalidElements.put(newName,
          new InvalidElementImpl(nestedElement,
              invalidElement.invalidValue(),
              invalidElement.violatedConstraints())
      );
    }
    return result;
  }

}
