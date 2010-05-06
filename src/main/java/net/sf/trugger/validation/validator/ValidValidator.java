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
package net.sf.trugger.validation.validator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.trugger.CreateException;
import net.sf.trugger.annotation.TargetElement;
import net.sf.trugger.bind.PostBind;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.impl.ElementDecorator;
import net.sf.trugger.message.Message;
import net.sf.trugger.message.MessagePart;
import net.sf.trugger.util.Utils;
import net.sf.trugger.validation.InvalidElement;
import net.sf.trugger.validation.Validation;
import net.sf.trugger.validation.ValidationBridge;
import net.sf.trugger.validation.ValidationContext;
import net.sf.trugger.validation.ValidationException;
import net.sf.trugger.validation.ValidationResult;
import net.sf.trugger.validation.Validator;
import net.sf.trugger.validation.validator.Valid.DEFAULT;

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
        strategy = annotation.strategy().newInstance();
      } catch (InstantiationException e) {
        throw new ValidationException(new CreateException(e));
      } catch (IllegalAccessException e) {
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

  private static class NestedInvalidElement extends ElementDecorator implements InvalidElement {

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

    public String toString() {
      return element.toString();
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((element == null) ? 0 : element.hashCode());
      result = prime * result + ((path == null) ? 0 : path.hashCode());
      result = prime * result + ((root == null) ? 0 : root.hashCode());
      return result;
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
