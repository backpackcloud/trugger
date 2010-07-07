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
package net.sf.trugger.ui.swing;

import java.util.Set;

import net.sf.trugger.Resolver;
import net.sf.trugger.annotation.Bind;
import net.sf.trugger.bind.Binder;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.Elements;


/**
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
public class SwingBinder {

  private SwingBinder() {}

  private static Resolver<Object, Element> element(final String name, final Object target) {
    return new Resolver<Object, Element>() {
      public Object resolve(Element element) {
        return Elements.element(name).in(target).value();
      }
    };
  }

  public static Binder newBinderForUI(Object swingComponent, Object target) {
    Binder binder = net.sf.trugger.bind.Bind.newBind();
    Set<Element> elements = Elements.elements().annotatedWith(Bind.class).in(swingComponent);
    for (Element element : elements) {
      String elementName = element.getAnnotation(Bind.class).to();
      if(elementName.isEmpty()) {
        elementName = element.name();
      }
      binder.use(element(elementName, target)).toElement(element.name());
    }
    return binder;
  }

}
