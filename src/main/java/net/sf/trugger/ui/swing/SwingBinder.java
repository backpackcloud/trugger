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

  /**
   * @author Marcelo Varella Barca Guimarães
   * @since 2.7
   */
  public interface UIComponentSpecifier {
    public void toUIComponent(Object uiComponent);
  }

  /**
   * @author Marcelo Varella Barca Guimarães
   * @since 2.7
   */
  public interface ObjectSpecifier {
    public void toObject(Object object);
  }

  enum Mode {
    UI_TO_OBJECT, OBJECT_TO_UI
  }

  private final Mode mode;
  private final Object target;
  private final Object uiTarget;
  private final String rootElement;
  private final String uiRootElement;

  private SwingBinder(Mode mode, Object target, Object uiTarget, String rootElement, String uiRootElement) {
    this.mode = mode;
    this.target = target;
    this.uiTarget = uiTarget;
    this.rootElement = rootElement;
    this.uiRootElement = uiRootElement;
  }

  public SwingBinder(Mode mode, Object target, Object uiComponent) {
    this(mode, target, uiComponent, null, null);
  }

  protected void configureBinder(Binder binder, Object uiComponent) {
    Set<Element> elements = Elements.elements().annotatedWith(Bind.class).in(uiComponent);

    for (Element element : elements) {
      String targetElementName = element.getAnnotation(Bind.class).to();
      String uiElementName = element.name();
      if (targetElementName.isEmpty()) {
        targetElementName = element.name();
      }
      if (rootElement != null) {
        targetElementName = rootElement + "." + targetElementName;
      }
      if (uiRootElement != null) {
        uiElementName = uiRootElement + "." + uiElementName;
      }
      if (element.type().isAnnotationPresent(SwingBind.class)) {
        SwingBinder swingBinder = new SwingBinder(mode, target, uiTarget, targetElementName, uiElementName);
        swingBinder.configureBinder(binder, element.type());
      } else {
        configureBind(binder, uiElementName, targetElementName);
      }
    }
  }

  private void configureBind(Binder binder, String uiComponentPath, String targetPath) {
    switch (mode) {
      case OBJECT_TO_UI:
        binder.use(element(targetPath, target)).toElement(uiComponentPath);
        break;
      case UI_TO_OBJECT:
        binder.use(element(uiComponentPath, uiTarget)).toElement(targetPath);
        break;
    }
  }

  private static Resolver<Object, Element> element(final String name, final Object target) {
    return new Resolver<Object, Element>() {

      public Object resolve(Element element) {
        Object resolved = Elements.element(name).in(target).value();
        return resolved;
      }
    };
  }

  public static UIComponentSpecifier bindObject(final Object object) {
    return new UIComponentSpecifier() {

      @Override
      public void toUIComponent(Object uiComponent) {
        Binder binder = net.sf.trugger.bind.Bind.newBinder();
        new SwingBinder(Mode.OBJECT_TO_UI, object, uiComponent).configureBinder(binder, uiComponent);
        binder.applyBinds(uiComponent);
      }
    };
  }

  public static ObjectSpecifier bindUIComponent(final Object uiComponent) {
    return new ObjectSpecifier() {

      @Override
      public void toObject(Object object) {
        Binder binder = net.sf.trugger.bind.Bind.newBinder();
        new SwingBinder(Mode.UI_TO_OBJECT, object, uiComponent).configureBinder(binder, uiComponent);
        binder.applyBinds(object);
      }
    };
  }

}
