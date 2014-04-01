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
import java.util.List;

import static org.atatec.trugger.util.factory.ParameterPredicates.name;
import static org.atatec.trugger.util.factory.ParameterPredicates.type;

/**
 * A factory to create components specified by annotations.
 * <p>
 * Given the example below:
 * <p>
 * <pre>
 *  @ValidatorClass(NotNullValidator.class)
 *  public @interface NotNull {
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
  }

  /**
   * Tries to create the component based on the given annotation.
   * <p>
   * If the annotation type <code>T</code> is present in the given annotation,
   * then the implementation defined will be instantiated.
   * <p>
   * The context for creating the component is composed by the elements of the
   * annotation and the annotation itself. For example:
   * <p>
   * <pre>
   *   @ComponentClass(MyComponent.class)
   *   public @interface MyAnnotation {
   *     boolean check();
   *     String name();
   *   }
   *
   *   @MyAnnotation(check = false, name = "someName")
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
   * @param annotation the annotation to check if the component can be created.
   * @return the created component
   * @throws CreateException if the component cannot be created
   */
  public E create(Annotation annotation) {
    Class type = annotation.annotationType();
    if (type.isAnnotationPresent(annotationType)) {
      Annotation classAnnotation = type.getAnnotation(annotationType);
      Element element = Elements.element(classElement).in(classAnnotation);
      Class<? extends E> typeToCreate = element.get();
      return create(annotation, typeToCreate);
    }
    throw new CreateException("Could not create an object");
  }

  private E create(Annotation annotation, Class<? extends E> classToCreate) {
    ContextFactory factory = new ContextFactory();
    Context context = factory.context();
    context.put(annotation, type(annotation.annotationType()));
    List<Element> elements = Elements.elements().in(annotation);
    for (Element el : elements) {
      context.put(() -> el.get(), name(el.name()).and(type(el.type())));
    }
    return factory.create(classToCreate);
  }

}
