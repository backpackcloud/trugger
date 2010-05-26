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
 * A binder that allows the use of any Seam component in a validator.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.6
 */
public class SeamValidatorBinder implements ValidatorBinder, Resolver<Object, Element> {

  @Override
  public void configureBinds(Validator validator, ValidatorContext context, Binder binder) {
    binder.use(this).toElements().annotatedWith(In.class);
  }

  @Override
  public Object resolve(Element target) {
    In annotation = target.getAnnotation(In.class);
    String name = resolveComponentName(target, annotation);
    return resolveComponent(name, annotation);
  }

  /**
   * Resolves the named component for the given annotation.
   * <p>
   * This implementation uses the {@link Component#getInstance(String, boolean)}
   * method.
   *
   * @param name
   *          the component name
   * @param annotation
   *          the annotation for resolving the component
   * @return the component to bind
   */
  protected Object resolveComponent(String name, In annotation) {
    return Component.getInstance(name, annotation.create());
  }

  /**
   * Resolves the component name based on the given annotation.
   * <p>
   * This implementation uses the {@link In#value()} property as the name or the
   * {@link Element#name() element name} if the property is empty.
   *
   * @param element
   *          the element annotated with {@link In}.
   * @param annotation
   *          the element annotation.
   * @return the component name.
   */
  protected String resolveComponentName(Element element, In annotation) {
    String name = annotation.value();
    if (Utils.isEmpty(name)) {
      name = element.name();
    }
    return name;
  }

}
