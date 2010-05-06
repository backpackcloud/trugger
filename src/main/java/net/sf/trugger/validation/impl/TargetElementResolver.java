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
package net.sf.trugger.validation.impl;

import net.sf.trugger.Resolver;
import net.sf.trugger.annotation.TargetElement;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.Elements;
import net.sf.trugger.util.Utils;
import net.sf.trugger.validation.ValidatorContext;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class TargetElementResolver implements Resolver<Object, Element> {

  private final ValidatorContext context;

  public TargetElementResolver(ValidatorContext context) {
    this.context = context;
  }

  public Object resolve(Element element) {
    TargetElement annotation = element.getAnnotation(TargetElement.class);
    boolean returnElement = Element.class.isAssignableFrom(element.type());
    String name = annotation.value();
    Element result;
    if (Utils.isEmpty(name)) {
      result = context.element();
    } else {
      result = Elements.element(name).in(context.target());
    }
    return returnElement ? result : result.in(context.target()).value();
  }

}
