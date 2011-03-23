/*
 * Copyright 2009-2011 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.validation;

import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import net.sf.trugger.annotation.AcceptedArrayTypes;
import net.sf.trugger.annotation.AcceptedTypes;
import net.sf.trugger.annotation.Reference;
import net.sf.trugger.annotation.TargetAnnotation;
import net.sf.trugger.annotation.TargetElement;
import net.sf.trugger.annotation.TargetObject;
import net.sf.trugger.loader.ImplementationLoader;
import net.sf.trugger.message.MessageCreator;
import net.sf.trugger.message.Messages;
import net.sf.trugger.validation.adapter.HibernateValidatorFactory;
import net.sf.trugger.validation.impl.CompositeValidatorBinder;
import net.sf.trugger.validation.impl.CompositeValidatorFactory;

/**
 * This class uses a {@link ValidationEngine validation engine} for validate
 * properties of a target by using default implementations of the components
 * required.
 * <p>
 * There are a set of features that can be useful for developing a Validator and
 * depends on the implementations for the {@link ValidationEngine},
 * {@link ValidatorFactory} and {@link ValidatorBinder} (all the listed features
 * are present in the default components):
 * <p>
 * <ul>
 * <li>If the validation must use a reference value present in the target
 * object. Just annotate a <i>String</i> property in the annotation that
 * indicates the element that must be binded with the {@link Reference}
 * annotation. The reference can also have their specific validation in the
 * property of the annotation.
 * <li>If the validation requires direct access to the target, it will be bind
 * in the element that has the {@link TargetObject} annotation.
 * <li>If the validation requires direct access to the {@link ValidationEngine},
 * an instance of {@link ValidationBridge} will be injected in the validator if
 * there is a compatible element.
 * <li>If the validator requires the element that has being validated, this
 * element will be injected in the element annotated with {@link TargetElement}.
 * <li>If the validator requires the annotation in the element that has being
 * validated, it will be injected in the compatible element of the validator
 * and/or in the element annotated with {@link TargetAnnotation} (useful for
 * generic validators).
 * <li>To forbid a type that is not supported by the validator, you can use the
 * generic type (if the allowed type is only one) or the annotations
 * {@link AcceptedTypes} and/or {@link AcceptedArrayTypes} in the class
 * declaration in case of more than one.
 * <li>If you need to use other validators inside yours, you can declare an
 * element of tipe {@link Validator} and annotate it with the desired
 * validations. You can use more than one annotation.
 * <li>Validations may have a named context (configured by the property
 * "context" in any annotation). If a context is
 * {@link ValidationEngine#forContext(String) specified}, only the annotations
 * with the specified context or with no context will be processed.
 * </ul>
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
public class Validation {

  private static final ValidationFactory factory;

  private static final List<ValidatorFactory> pluggedFactories = new LinkedList<ValidatorFactory>();
  private static final List<ValidatorBinder> pluggedBinders = new LinkedList<ValidatorBinder>();

  static {
    factory = ImplementationLoader.getInstance().get(ValidationFactory.class);
    pluggedBinders.add(factory.createValidatorBinder());
    pluggedFactories.add(factory.createValidatorFactory(newValidatorBinder()));
  }

  private final MessageCreator messageCreator;

  /**
   * Creates a new object validator.
   */
  public Validation() {
    messageCreator = Messages.createMessageCreator();
  }

  /**
   * Creates a new object validator using the specified bundle.
   *
   * @param bundle
   *          the resource bundle for resolving the messages.
   */
  public Validation(ResourceBundle bundle) {
    messageCreator = Messages.createMessageCreator(bundle);
  }

  /**
   * Creates a new object validator using the given message creator.
   *
   * @param messageCreator
   *          the component for creating the validation messages.
   */
  public Validation(MessageCreator messageCreator) {
    this.messageCreator = messageCreator;
  }

  /**
   * Starts the configuration for the validation.
   *
   * @return a reference for the validation component.
   */
  public final ValidationEngine validate() {
    return factory.createValidationEngine(newValidatorFactory(), messageCreator);
  }

  /**
   * Plugs a ValidatorFactory for using with the implementation. Use this method
   * to include support for other validators (such as HibernateValidator)
   * without affect the implementation.
   * <p>
   * Make sure that no validation is doing while calling this method because it
   * is not thread-safe.
   *
   * @see HibernateValidatorFactory
   */
  public static void plug(ValidatorFactory validatorFactory) {
    synchronized (pluggedFactories) {
      pluggedFactories.add(validatorFactory);
    }
  }

  /**
   * Unplugs the given ValidatorFactory.
   * <p>
   * Make sure that no validation is doing while calling this method because it
   * is not thread-safe.
   */
  public static void unplug(ValidatorFactory validatorFactory) {
    synchronized (pluggedFactories) {
      pluggedFactories.remove(validatorFactory);
    }
  }

  /**
   * Plugs the given binder.
   * <p>
   * Make sure that no validation is doing while calling this method because it
   * is not thread-safe.
   *
   * @since 2.4
   */
  public static void plug(ValidatorBinder validatorBinder) {
    synchronized (pluggedBinders) {
      pluggedBinders.add(validatorBinder);
    }
  }

  /**
   * Unplus the given binder.
   * <p>
   * Make sure that no validation is doing while calling this method because it
   * is not thread-safe.
   *
   * @since 2.4
   */
  public static void unplug(ValidatorBinder validatorBinder) {
    synchronized (validatorBinder) {
      pluggedBinders.remove(validatorBinder);
    }
  }

  /**
   * Creates a new ValidatorFactory based on the
   * {@link ValidationFactory#createValidatorFactory(ValidatorBinder) implementation} and the
   * {@link #plug(ValidatorFactory) pluggeds}.
   * <p>
   * Every factory plugged after will be used by the returning ValidatorFactory.
   *
   * @return the created factory.
   */
  public static ValidatorFactory newValidatorFactory() {
    return new CompositeValidatorFactory(pluggedFactories);
  }

  /**
   * Creates a new ValidatorBinder based on the
   * {@link ValidationFactory#createValidatorBinder() implementation} and the
   * {@link #plug(ValidatorBinder) pluggeds}.
   * <p>
   * Every binder plugged after will be used by the returning ValidatorBinder.
   *
   * @return the created binder
   * @since 2.4
   */
  public static ValidatorBinder newValidatorBinder() {
    return new CompositeValidatorBinder(pluggedBinders);
  }

}
