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
import org.atatec.trugger.interception.Interception;
import org.atatec.trugger.interception.ValidationInterceptionHandler;
import org.atatec.trugger.util.factory.ComponentFactory;
import org.atatec.trugger.validation.ValidationEngine;
import org.atatec.trugger.validation.Validator;
import org.atatec.trugger.validation.ValidatorClass;
import org.atatec.trugger.validation.ValidatorFactory;

import java.lang.annotation.Annotation;

import static org.atatec.trugger.reflection.ParameterPredicates.assignableTo;
import static org.atatec.trugger.reflection.ParameterPredicates.type;
import static org.atatec.trugger.reflection.Reflection.invoke;

/**
 * Default validator factory implementation.
 *
 * @since 5.1
 */
public class TruggerValidatorFactory implements ValidatorFactory {

  private ComponentFactory<ValidatorClass, Validator> createFactory() {
    ComponentFactory<ValidatorClass, Validator> factory =
        new ComponentFactory<>(ValidatorClass.class);
    factory.toCreate(
        (constructor, args) -> {
          Validator validator = invoke(constructor).withArgs(args);
          return Interception.intercept(Validator.class)
              .on(validator)
              .onCall(new ValidationInterceptionHandler(this)
                  .onInvalid(context -> true))
              .proxy();
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
        }
    ));
    return factory.create(annotation);
  }

}
