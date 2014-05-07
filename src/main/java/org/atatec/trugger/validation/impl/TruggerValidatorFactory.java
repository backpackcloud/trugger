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
import org.atatec.trugger.element.ElementPredicates;
import org.atatec.trugger.element.Elements;
import org.atatec.trugger.interception.Interception;
import org.atatec.trugger.interception.ValidationInterceptionHandler;
import org.atatec.trugger.util.factory.ComponentFactory;
import org.atatec.trugger.validation.*;

import java.lang.annotation.Annotation;
import java.util.List;

import static org.atatec.trugger.reflection.ParameterPredicates.assignableTo;
import static org.atatec.trugger.reflection.ParameterPredicates.named;
import static org.atatec.trugger.reflection.ParameterPredicates.type;
import static org.atatec.trugger.reflection.Reflection.invoke;

/**
 * Default validator factory implementation.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public class TruggerValidatorFactory implements ValidatorFactory {

  private ComponentFactory<ValidatorClass, Validator> createFactory() {
    ComponentFactory<ValidatorClass, Validator> factory =
        new ComponentFactory<>(ValidatorClass.class);
    factory.toCreate(
        (constructor, args) -> {
          ArgumentsValidator argumentsValidator = new ArgumentsValidator(this);
          if (argumentsValidator.isValid(constructor, args)) {
            Validator validator = invoke(constructor).withArgs(args);
            return Interception.intercept(Validator.class)
                .on(validator)
                .onCall(new ValidationInterceptionHandler(this)
                    .onInvalid(context -> true))
                .proxy();
          }
          // there is no way to validate the value using this validator
          // so lets say that the value is not invalid (this is not the same
          // as saying that the value is valid according to the validation
          // API definitions)
          return (Validator) value -> true;
        }
    );
    return factory;
  }

  @Override
  public Validator create(Annotation annotation) {
    ComponentFactory<ValidatorClass, Validator> factory = createFactory();
    return factory.create(annotation);
  }

  @Override
  public Validator create(Annotation annotation, Element element,
                          Object target, ValidationEngine engine) {
    ComponentFactory<ValidatorClass, Validator> factory = createFactory();
    factory.toConfigure(ComponentFactory.defaults().andThen(
        (context, an) -> {
          context.use(element).when(type(Element.class));
          context.use(engine).when(type(ValidationEngine.class));
          context.use(target).when(assignableTo(target.getClass())

              .or(type(Object.class)));
          context.use(an).when(type(Annotation.class));
          context.use(TruggerValidatorFactory.this)
              .when(type(ValidatorFactory.class));

          List<Element> elements = Elements.elements().in(annotation);
          elements.stream()
              .filter(ElementPredicates.annotatedWith(TargetElement.class))
              .forEach(annotationElement -> {
                TargetElement targetElement = annotationElement
                    .getAnnotation(TargetElement.class);
                Object value = Elements.element(annotationElement.get())
                    .in(target).get();
                context.use(value).when(assignableTo(targetElement.value())
                    .or(named(annotationElement.name())));
              });
          // force injection when "-parameters" are not used in compilation
          elements.stream().forEach(
              el -> context.use(() -> el.get()).when(type(el.type()))
          );
        }
    ));
    return factory.create(annotation);
  }

}
