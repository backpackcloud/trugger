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
package net.sf.trugger.annotation.processors;

import java.lang.annotation.Annotation;
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
public class BindProcessor {

  /**
   * @author Marcelo Varella Barca Guimarães
   * @since 2.7
   */
  public interface ContextSpecifier {

    public void toContext(Object context);
  }

  /**
   * @author Marcelo Varella Barca Guimarães
   * @since 2.7
   */
  public interface ObjectSpecifier {

    public void toObject(Object object);
  }

  enum Mode {
    CONTEXT_TO_OBJECT, OBJECT_TO_CONTEXT
  }

  private Mode mode;
  private Object target;
  private Object contextTarget;
  private String rootElement;
  private String contextRootElement;
  private Class<? extends Annotation> annotationType;

  public BindProcessor() {

  }

  public BindProcessor(Class<? extends Annotation> annotationType) {
    this.annotationType = annotationType;
  }

  private void configureBinder(Binder binder, Object context) {
    Set<Element> elements = Elements.elements().annotatedWith(Bind.class).in(context);

    for (Element element : elements) {
      String targetElementName = element.getAnnotation(Bind.class).to();
      String contextElementName = element.name();
      if (targetElementName.isEmpty()) {
        targetElementName = element.name();
      }
      if (rootElement != null) {
        targetElementName = rootElement + "." + targetElementName;
      }
      if (contextRootElement != null) {
        contextElementName = contextRootElement + "." + contextElementName;
      }
      if (element.type().isAnnotationPresent(annotationType)) {
        BindProcessor processor = new BindProcessor(annotationType);
        processor.mode = mode;
        processor.target = target;
        processor.contextTarget = contextTarget;
        processor.rootElement = targetElementName;
        processor.contextRootElement = contextElementName;
        processor.configureBinder(binder, element.type());
      } else {
        configureBind(binder, contextElementName, targetElementName);
      }
    }
  }

  private void configureBind(Binder binder, String contextPath, String targetPath) {
    switch (mode) {
      case OBJECT_TO_CONTEXT:
        binder.use(element(targetPath, target)).toElement(contextPath);
        break;
      case CONTEXT_TO_OBJECT:
        binder.use(element(contextPath, contextTarget)).toElement(targetPath);
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

  public ContextSpecifier bindObject(final Object object) {
    return new ContextSpecifier() {

      @Override
      public void toContext(Object contextObject) {
        Binder binder = net.sf.trugger.bind.Bind.newBinder();
        mode = Mode.OBJECT_TO_CONTEXT;
        target = object;
        contextTarget = contextObject;
        configureBinder(binder, contextObject);
        binder.applyBinds(contextObject);
      }
    };
  }

  public ObjectSpecifier bindContext(final Object contextObject) {
    return new ObjectSpecifier() {

      @Override
      public void toObject(Object object) {
        Binder binder = net.sf.trugger.bind.Bind.newBinder();
        mode = Mode.CONTEXT_TO_OBJECT;
        target = object;
        contextTarget = contextObject;
        configureBinder(binder, contextObject);
        binder.applyBinds(object);
      }
    };
  }

}
