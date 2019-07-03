/*
 * The Apache License
 *
 * Copyright 2009 Marcelo Guimaraes <ataxexe@backpackcloud.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.backpackcloud.trugger.util.factory;

import io.backpackcloud.trugger.element.Element;
import io.backpackcloud.trugger.element.Elements;
import io.backpackcloud.trugger.reflection.Reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static io.backpackcloud.trugger.reflection.ParameterPredicates.ofName;
import static io.backpackcloud.trugger.reflection.ParameterPredicates.ofType;

/**
 * A factory to create components specified by annotations. This factory relies on
 * a {@link Context} object to create the components.
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
 * @param <T> The type of the annotation
 * @param <E> The type of the component
 * @since 5.0
 */
public class ComponentFactory<T extends Annotation, E> {

  private final Class<T> annotationType;
  private final String classElement;
  private final BiConsumer<Context, Annotation> contextConsumer;
  private final BiFunction<Constructor, Object[], Object> createFunction;

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
    this(annotationType,
        classElement,
        (context, annotation) -> {
        },
        (constructor, args) -> Reflection.invoke(constructor).withArgs(args));
  }

  public ComponentFactory(Class<T> annotationType,
                          String classElement,
                          BiConsumer<Context, Annotation> contextConsumer,
                          BiFunction<Constructor, Object[], Object> createFunction) {
    this.annotationType = annotationType;
    this.classElement = classElement;
    this.contextConsumer = contextConsumer;
    this.createFunction = createFunction;
  }

  /**
   * Sets the function used to create the objects.
   *
   * @param createFunction the function to use
   * @return a new ComponentFactory
   */
  public ComponentFactory<T, E> toCreate(BiFunction<Constructor, Object[], Object>
                                             createFunction) {
    return new ComponentFactory<>(this.annotationType, this.classElement, this.contextConsumer, createFunction);
  }

  /**
   * Adds more rules to the context by passing a consumer that will be invoked before creating the
   * component.
   *
   * @param consumer the consumer to configure the context
   * @return a new ComponentFactory
   */
  public ComponentFactory<T, E> toConfigure(BiConsumer<Context, Annotation> consumer) {
    return new ComponentFactory<>(this.annotationType, this.classElement, consumer, this.createFunction);
  }

  /**
   * Tries to create the component based on the given annotation.
   * <p>
   * If the annotation type <code>T</code> is present in the given annotation,
   * then the implementation defined will be instantiated.
   *
   * @param annotation the annotation to check if the component can be created.
   * @return the created component if the annotation is not annotated with the required annotation.
   */
  public Optional<E> create(Annotation annotation) {
    Class<?> type = annotation.annotationType();
    if (type.isAnnotationPresent(annotationType)) {
      T classAnnotation = type.getAnnotation(annotationType);
      Element element = Elements.element(classElement).from(classAnnotation).get();
      Class<E> typeToCreate = element.getValue();
      return create(annotation, typeToCreate);
    }
    return Optional.empty();
  }

  /**
   * Creates a component using the first annotation that maps to a component.
   * <p>
   * Use this method if only one component is allowed by target.
   *
   * @param element the element to search for annotations
   * @return the created component or <code>null</code> if no annotations in the
   * element could create a component.
   */
  public Optional<E> create(AnnotatedElement element) {
    Optional<E> component;
    for (Annotation annotation : element.getAnnotations()) {
      component = create(annotation);
      if (component.isPresent()) {
        return component;
      }
    }
    return Optional.empty();
  }

  /**
   * Creates a list of components based on the annotations of the given element.
   *
   * @param element the element to search for annotations
   * @return a list of all components that can be instantiated from the
   * annotations present in the given element.
   * @see #create(java.lang.annotation.Annotation)
   */
  public List<E> createAll(AnnotatedElement element) {
    List<E> result = new ArrayList<>();
    for (Annotation annotation : element.getAnnotations()) {
      create(annotation).ifPresent(result::add);
    }
    return result;
  }

  private Optional<E> create(Annotation annotation, Class<E> classToCreate) {
    ContextFactory factory = new ContextFactory();
    Context context = factory.context();
    context.use(annotation).when(ofType(annotation.annotationType()));
    List<Element> elements = Elements.elements().from(annotation);
    for (Element el : elements) {
      context.use(el::getValue).when(ofName(el.name()).and(ofType(el.type())));
    }
    for (Element el : elements) {
      context.use(el::getValue).when(ofType(el.type()));
    }
    contextConsumer.accept(context, annotation);
    return factory.toCreate(createFunction).create(classToCreate);
  }

}
