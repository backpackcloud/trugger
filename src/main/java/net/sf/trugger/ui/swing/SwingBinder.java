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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import net.sf.trugger.Resolver;
import net.sf.trugger.annotation.Bind;
import net.sf.trugger.bind.BindSelector;
import net.sf.trugger.bind.Binder;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.Elements;
import net.sf.trugger.util.Utils;

/**
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
public class SwingBinder {

  enum Mode {
    UI_TO_OBJECT, OBJECT_TO_UI
  }

  private final Object swingComponent;
  private final Object target;
  private final Mode mode;

  private SwingBinder(Mode mode, Object swingComponent, Object target) {
    this.mode = mode;
    this.swingComponent = swingComponent;
    this.target = target;
  }

  public Binder newBinder() {
    Class<?> type = Utils.resolveType(swingComponent);
    if (!type.isAnnotationPresent(SwingBind.class)) {
      throw new IllegalArgumentException("The swing component does not have a SwingBind annotation.");
    }
    SwingBind annotation = type.getAnnotation(SwingBind.class);
    String parentElementName = annotation.to();
    final Collection<Binder> binders = initializeBinder(parentElementName, swingComponent);
    return new Binder() {

      Binder _binder = net.sf.trugger.bind.Bind.newBinder();

      @Override
      public BindSelector use(Resolver<Object, Element> resolver) {
        return _binder.use(resolver);
      }

      @Override
      public BindSelector bind(Object value) {
        return _binder.bind(value);
      }

      @Override
      public <E> E applyBinds(E object) {
        _binder.applyBinds(object);
        for (Binder binder : binders) {
          binder.applyBinds(object);
        }
        return object;
      }
    };
  }

  protected Collection<Binder> initializeBinder(String parentElementName, Object component) {
    Collection<Binder> binders = new ArrayList<Binder>();
    Binder binder = net.sf.trugger.bind.Bind.newBinder();
    binders.add(binder);
    boolean nested = !parentElementName.isEmpty();
    Set<Element> elements = Elements.elements().annotatedWith(Bind.class).in(component);
    for (Element element : elements) {
      String elementName = element.getAnnotation(Bind.class).to();
      if (elementName.isEmpty()) {
        elementName = element.name();
      }
      if (nested) {
        elementName = String.format("%s.%s", parentElementName, elementName);
      }
      if (element.type().isAnnotationPresent(SwingBind.class)) {
        binders.addAll(new SwingBinder(mode, element.type(), target).initializeBinder(elementName, element.type()));
      } else {
        configureBind(binder, element, elementName);
      }
    }
    return binders;
  }

  protected void configureBind(Binder binder, Element swingComponentElement, String elementNameToBind) {
    if (mode == Mode.OBJECT_TO_UI) {
      binder.use(element(elementNameToBind, target)).toElement(swingComponentElement.name());
    } else {
      binder.use(element(swingComponentElement.name(), swingComponent)).toElement(elementNameToBind);
    }
  }

  private static Resolver<Object, Element> element(final String name, final Object target) {
    return new Resolver<Object, Element>() {

      public Object resolve(Element element) {
        return Elements.element(name).in(target).value();
      }
    };
  }

  public static Binder newBinderForUI(Object swingComponent, Object target) {
    return new SwingBinder(Mode.OBJECT_TO_UI, swingComponent, target).newBinder();
  }

  public static Binder newBinderForTarget(Object swingComponent, Object target) {
    return new SwingBinder(Mode.UI_TO_OBJECT, swingComponent, target).newBinder();
  }

  public static void bindToUI(Object swingComponent, Object target) {
    newBinderForUI(swingComponent, target).applyBinds(swingComponent);
  }

  public static void bindToTarget(Object swingComponent, Object target) {
    newBinderForTarget(swingComponent, target).applyBinds(target);
  }

}
