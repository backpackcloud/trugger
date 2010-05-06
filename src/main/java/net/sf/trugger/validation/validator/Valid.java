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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;
import java.util.Map;

import net.sf.trugger.element.Element;
import net.sf.trugger.validation.ValidationBridge;
import net.sf.trugger.validation.ValidationEngine;
import net.sf.trugger.validation.ValidationResult;
import net.sf.trugger.validation.ValidatorClass;

/**
 * Indicates that the property is an object that must be valid using an
 * {@link ValidationEngine}.
 * <p>
 * If the property is a Collection, a Map or an Array, its elements will be
 * validated.
 * <p>
 * Note that the {@link ValidationEngine} used to validate the target is the
 * same that will be used to validate the annotated property.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@ValidatorClass(ValidValidator.class)
public @interface Valid {

  /**
   * Indicates if the validation messages must be added to the invalid
   * properties list. If this property is <code>false</code> only the
   * validator's message will be added to the list.
   * <p>
   * Note that this property will only take effect if the property is not a
   * collection, a map or an array.
   */
  boolean addMessages() default true;

  /**
   * Indicates the strategy for using to validate the object if it is a
   * collection (an array, {@link Collection} or a {@link Map}).
   * <p>
   * Leave it default to use the {@link DefaultValidationStrategy default
   * strategy}.
   *
   * @see ValidValidator#setDefaultValidationStrategy(ValidationStrategy)
   */
  Class<? extends ValidationStrategy> strategy() default DEFAULT.class;

  /**
   * The message for this validation or the name of the key that must be used to
   * get the message.
   */
  String message() default "validation.Valid";

  /**
   * The context that this validation belongs.
   */
  String[] context() default {};

  /**
   * The context for validating. Leave it empty for use the root context.
   */
  String forContext() default "";

  /**
   * A key class to indicate that the default strategy must be used.
   *
   * @author Marcelo Varella Barca Guimarães
   */
  static final class DEFAULT implements ValidationStrategy {

    private DEFAULT() {}

    public boolean breakOnFirstInvalidObject() {
      throw new Error();
    }

    public ValidationResult validateInCollection(int index, Object object, ValidationBridge bridge, Valid annotation,
        Element element) {
      throw new Error();
    }

    public ValidationResult validate(Object object, ValidationBridge bridge, Valid annotation, Element element) {
      throw new Error();
    }
  }

}
