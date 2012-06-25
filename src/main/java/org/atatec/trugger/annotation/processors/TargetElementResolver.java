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
package org.atatec.trugger.annotation.processors;

import org.atatec.trugger.Resolver;
import org.atatec.trugger.annotation.TargetElement;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.Elements;
import org.atatec.trugger.factory.AnnotationFactoryContext;
import org.atatec.trugger.util.Utils;

/**
 * A resolver for elements annotated with {@link TargetElement}.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
public class TargetElementResolver implements Resolver<Object, Element> {

  private final AnnotationFactoryContext context;

  public TargetElementResolver(AnnotationFactoryContext context) {
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
