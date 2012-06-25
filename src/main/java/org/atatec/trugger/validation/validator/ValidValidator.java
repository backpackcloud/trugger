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
package org.atatec.trugger.validation.validator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.atatec.trugger.CreateException;
import org.atatec.trugger.annotation.TargetElement;
import org.atatec.trugger.bind.PostBind;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.impl.DecoratedElement;
import org.atatec.trugger.message.Message;
import org.atatec.trugger.message.MessagePart;
import org.atatec.trugger.reflection.Reflection;
import org.atatec.trugger.reflection.ReflectionException;
import org.atatec.trugger.util.HashBuilder;
import org.atatec.trugger.util.Utils;
import org.atatec.trugger.validation.InvalidElement;
import org.atatec.trugger.validation.Validation;
import org.atatec.trugger.validation.ValidationBridge;
import org.atatec.trugger.validation.ValidationContext;
import org.atatec.trugger.validation.ValidationException;
import org.atatec.trugger.validation.ValidationResult;
import org.atatec.trugger.validation.Validator;
import org.atatec.trugger.validation.validator.Valid.DEFAULT;

/**
 * Implementation of the {@link Valid} validation.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
public class ValidValidator implements Validator<Object> {

  private static ValidationStrategy defaultValidationStrategy = new DefaultValidationStrategy();

  private ValidationBridge bridge;
  private Valid annotation;
  @TargetElement
  private Element element;
  private ValidationStrategy strategy;
  @ValidationContext
  private String validationContext;

  @PostBind
  private void initialize() {
    if (DEFAULT.class.equals(annotation.strategy())) {
      strategy = defaultValidationStrategy;
    } else {
      try {
        strategy = Reflection.newInstanceOf(annotation.strategy());
      } catch (ReflectionException e) {
        throw new ValidationException(new CreateException(e));
      }
    }
  }

  public boolean isValid(@NotNull Object value) {
    if (value instanceof Collection) {
      return isValid((Collection<?>) value);
    } else if (value instanceof Map) {
      return isValid(((Map<?, ?>) value).values());
    } else if (value.getClass().isArray()) {
      return isValid(Arrays.asList((Object[]) value));
    }
    if(bridge == null) {
      return validate(value);
    }
    ValidationResult result = strategy.validate(value, bridge, annotation, element);
    if (annotation.addMessages()) {
      Collection<InvalidElement> invalidElements = result.invalidElements();
      for (InvalidElement invalidElement : invalidElements) {
        bridge.addInvalidElement(new NestedInvalidElement(invalidElement, element));
      }
    }
    return result.isValid();
  }

  private boolean validate(Object value) {
    String context = annotation.forContext();
    if(Utils.isEmpty(context)) {
      context = validationContext;
    }
    ValidationResult result;
    if (Utils.isEmpty(context)) {
      result = new Validation().validate().allElements().in(value);
    } else {
      result = new Validation().validate().forContext(context).allElements().in(value);
    }
    return result.isValid();
  }

  private boolean isValid(Collection<?> collection) {
    boolean result = true;
    int i = 0;
    for (Object object : collection) {
      if (strategy.validateInCollection(i++, object, bridge, annotation, element).isInvalid()) {
        if (strategy.breakOnFirstInvalidObject()) {
          return false;
        }
        result = false;
      }
    }
    return result;
  }

  /**
   * Sets the default validation strategy to be used if the {@link Valid
   * annotation} has the {@link DEFAULT default class} as the strategy.
   *
   * @param strategy
   *          the strategy to use.
   */
  public static void setDefaultValidationStrategy(ValidationStrategy strategy) {
    defaultValidationStrategy = strategy;
  }

  private static class NestedInvalidElement extends DecoratedElement implements InvalidElement {

    private final InvalidElement element;
    private final Element root;
    private final String path;

    NestedInvalidElement(InvalidElement element, Element rootElement) {
      super(element);
      this.element = element;
      this.path = String.format("%s.%s", rootElement.name(), element.name());
      this.root = rootElement;
    }

    public Class<?> declaringClass() {
      return root.declaringClass();
    }

    public String path() {
      return path;
    }

    public List<Message> messages() {
      return element.messages();
    }

    public String joinMessages(String messageSeparator, MessagePart type) {
      return element.joinMessages(messageSeparator, type);
    }

    public Object invalidValue() {
      return element.invalidValue();
    }

    @Override
    public int hashCode() {
      return new HashBuilder(element).add(path).add(root).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!super.equals(obj)) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      NestedInvalidElement other = (NestedInvalidElement) obj;
      if (element == null) {
        if (other.element != null) {
          return false;
        }
      } else if (!element.equals(other.element)) {
        return false;
      }
      if (path == null) {
        if (other.path != null) {
          return false;
        }
      } else if (!path.equals(other.path)) {
        return false;
      }
      if (root == null) {
        if (other.root != null) {
          return false;
        }
      } else if (!root.equals(other.root)) {
        return false;
      }
      return true;
    }

  }

}
