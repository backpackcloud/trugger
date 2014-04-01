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

package org.atatec.trugger.util.factory;

import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.Elements;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static org.atatec.trugger.util.factory.ParameterPredicates.name;
import static org.atatec.trugger.util.factory.ParameterPredicates.type;

/**
 * A factory to create components specified by annotations.
 * <p>
 * Given the example below:
 * <p>
 * <pre>
 *  &#64;ValidatorClass(NotNullValidator.class)
 *  public &#64;interface NotNull {
 *
 *  }
 * </pre>
 * <p>
 * An instance of a <code>NotNullValidator</code> can be created using something
 * annotated with the <code>NotNull</code> annotation just by passing the
 * annotated element to this factory.
 *
 * @param <T> The type of the annotation
 * @param <E> The type of the component
 * @since 5.0
 */
public class ComponentFactory<T extends Annotation, E> {

  private final Class<T> annotationType;
  private final String classElement;
  private BiConsumer<Context, Annotation> contextConsumer;

  /**
   * Creates a new factory that searches for the implementation by looking at
   * the "value" property of the annotation.
   *
   * @param annotationType the type of the annotation to search
   */
  public ComponentFactory(Class<T> annotationType) {
    this(annotationType, "value");
  }

  /**
   * Creates a new factory that searches for the implementation by looking at
   * the given property name of the annotation.
   *
   * @param annotationType the type of the annotation to search
   * @param classElement   the property name that contains the implementation
   */
  public ComponentFactory(Class<T> annotationType, String classElement) {
    this.annotationType = annotationType;
    this.classElement = classElement;
    configureContextWith(defaults());
  }

  /**
   * Changes the way the context is configured by replacing the consumer.
   * <p>
   * You can add behaviour by composing the default consumer:
   * <p>
   * <pre>
   *   factory.configureContextWith(
   *     defaults().andThen(
   *       (context, annotation) -&gt; yourConfigurations
   *     )
   *   );
   * </pre>
   *
   * @param consumer the consumer to use for configuring the context to create
   *                 the components.
   * @return a reference to this object
   * @see #defaults()
   */
  public ComponentFactory configureContextWith(
      BiConsumer<Context, Annotation> consumer) {
    this.contextConsumer = consumer;
    return this;
  }

  /**
   * Tries to create the component based on the given annotation.
   * <p>
   * If the annotation type <code>T</code> is present in the given annotation,
   * then the implementation defined will be instantiated.
   *
   * @param annotation the annotation to check if the component can be created.
   * @return the created component or <code>null</code> if the annotation is not
   * annotated with the required annotation.
   */
  public E create(Annotation annotation) {
    Class<?> type = annotation.annotationType();
    if (type.isAnnotationPresent(annotationType)) {
      T classAnnotation = type.getAnnotation(annotationType);
      Element element = Elements.element(classElement).in(classAnnotation);
      Class<? extends E> typeToCreate = element.get();
      return create(annotation, typeToCreate);
    }
    return null;
  }

  /**
   * Creates a list of components based on the annotations of the given element.
   *
   * @param element the element to search for annotations
   * @return a list of all components that can be instantiated from the
   * annotations present in the given element.
   * @see #create(java.lang.annotation.Annotation)
   */
  public List<E> create(AnnotatedElement element) {
    List result = new ArrayList<>();
    E component;
    for (Annotation annotation : element.getAnnotations()) {
      component = create(annotation);
      if (component != null) {
        result.add(component);
      }
    }
    return result;
  }

  private E create(Annotation annotation, Class<? extends E> classToCreate) {
    ContextFactory factory = new ContextFactory();
    Context context = factory.context();
    contextConsumer.accept(context, annotation);
    return factory.create(classToCreate);
  }

  /**
   * Returns the default consumer to configure the context for creating
   * components.
   * <p>
   * The context for creating the component is composed by the elements of the
   * annotation and the annotation itself. For example:
   * <p>
   * <pre>
   *   &#64;ComponentClass(MyComponent.class)
   *   public &#64;interface MyAnnotation {
   *     boolean check();
   *     String name();
   *   }
   *
   *   &#64;MyAnnotation(check = false, name = "someName")
   *   private String someField;
   * </pre>
   * <p>
   * A context to create the component will have:
   * <p>
   * <ul>
   * <li>the value <code>false</code> mapped to a boolean parameter named
   * "check"</li>
   * <li>the value <code>someName</code> mapped to a String parameter named
   * "name"</li>
   * <li>the annotation <code>MyAnnotation</code> itself mapped to a parameter
   * of the same type</li>
   * </ul>
   *
   * @return the default consumer to configure the context
   */
  public static BiConsumer<Context, Annotation> defaults() {
    return (context, annotation) -> {
      context.put(annotation, type(annotation.annotationType()));
      List<Element> elements = Elements.elements().in(annotation);
      for (Element el : elements) {
        context.put(() -> el.get(), name(el.name()).and(type(el.type())));
      }
    };
  }

}
