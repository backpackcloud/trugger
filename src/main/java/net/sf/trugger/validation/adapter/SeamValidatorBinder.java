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
package net.sf.trugger.validation.adapter;

import net.sf.trugger.Resolver;
import net.sf.trugger.bind.Binder;
import net.sf.trugger.element.Element;
import net.sf.trugger.util.Utils;
import net.sf.trugger.validation.Validator;
import net.sf.trugger.validation.ValidatorBinder;
import net.sf.trugger.validation.ValidatorContext;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;

/**
 * A binder that allows the use of any Seam component inside of a validator.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.6
 */
public class SeamValidatorBinder implements ValidatorBinder {

  @Override
  public void configureBinds(Validator validator, ValidatorContext context, Binder binder) {
    binder.bind(new InResolver()).toElements().annotatedWith(In.class);
  }

  private class InResolver implements Resolver<Object, Element> {

    public Object resolve(Element target) {
      In annotation = target.getAnnotation(In.class);
      String name = annotation.value();
      if (Utils.isEmpty(name)) {
        name = target.name();
      }
      return Component.getInstance(name, annotation.create());
    }

  }

}
