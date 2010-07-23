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

import static net.sf.trugger.element.Elements.elements;

import java.lang.annotation.Annotation;
import java.util.Set;

import net.sf.trugger.Resolver;
import net.sf.trugger.annotation.Bind;
import net.sf.trugger.bind.Binder;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.Elements;
import net.sf.trugger.format.Formatter;
import net.sf.trugger.transformer.BidirectionalTransformer;
import net.sf.trugger.util.Utils;

/**
 * A class that process {@link Bind} annotations.
 * <p>
 * The object annotated with {@link Bind} will be the context object. The
 * annotated elements may have annotations for {@link Formatter formatter}
 * and/or {@link BidirectionalTransformer transformer}.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
public class BindProcessor {

  /**
   * Interface for specifying the context object.
   *
   * @author Marcelo Varella Barca Guimarães
   * @since 2.7
   */
  public interface ContextSpecifier {

    /**
     * Defines the context object for injecting values.
     *
     * @param context
     *          the context object
     */
    public void toContext(Object context);
  }

  /**
   * Interface for specifying the target object.
   *
   * @author Marcelo Varella Barca Guimarães
   * @since 2.7
   */
  public interface ObjectSpecifier {

    /**
     * Defines the target object.
     *
     * @param object
     *          the target object.
     */
    public void toObject(Object object);
  }

  private enum Mode {
    CONTEXT_TO_OBJECT, OBJECT_TO_CONTEXT
  }

  private Mode mode;
  private Object target;
  private Object contextTarget;
  private String rootElement;
  private String contextRootElement;
  private Class<? extends Annotation> annotationType;

  /**
   * Creates a new BindProcessor
   */
  public BindProcessor() {

  }

  /**
   * Creates a new BindProcessor that makes deep searchs for binds in all
   * elements annotated with the given annotation type.
   *
   * @param annotationType
   *          the annotation type that defines a deep search.
   */
  public BindProcessor(Class<? extends Annotation> annotationType) {
    this.annotationType = annotationType;
  }

  private void configureBinder(Binder binder, Object context) {
    Bind contextAnnotation = Utils.resolveType(context).getAnnotation(Bind.class);
    Set<Element> elements;
    if (contextAnnotation == null) {
      elements = elements().annotatedWith(Bind.class).in(context);
    } else {
      elements = elements().in(context);
    }

    for (Element element : elements) {
      Bind annotation = element.getAnnotation(Bind.class);
      if (annotation == null) {
        annotation = contextAnnotation;
      }
      String targetElementName = annotation.to();
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

  /**
   * Binds object contents to the context object.
   *
   * @param object
   *          the object to use.
   * @return a component to select the context object.
   */
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

  /**
   * Binds context object contents to a target object.
   *
   * @param contextObject
   *          the context object to use.
   * @return a component to select the target object.
   */
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
