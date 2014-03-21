/*
 * Copyright 2009-2012 Marcelo Guimarães
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
package org.atatec.trugger.util.mock;

import org.atatec.trugger.interception.Interception;
import org.atatec.trugger.interception.InterceptionContext;
import org.atatec.trugger.interception.Interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import static org.atatec.trugger.reflection.Reflection.reflect;
import static org.atatec.trugger.reflection.ReflectionPredicates.named;

/**
 * A builder for creating mock {@link Annotation annotations}.
 * <p/>
 * This mock provides some useful features because annotations may have default values.
 * This class can resolve them and automatically configure the values in the mock object.
 * <p/>
 * Here is an example of use:
 * <p/>
 * <pre>
 * import static org.atatec.trugger.util.mock.Mock.annotation;
 * import static org.atatec.trugger.util.mock.Mock.mock;
 * import javax.annotation.Resource;
 * // ...
 * Resource resource = new AnnotationMock&lt;Resource&gt;(){{
 *       map(&quot;name&quot;).to(annotation.name());
 *       map(false).to(annotation.shareable());
 *     }}.mock();
 * String name = resource.name(); //returns &quot;name&quot;
 * boolean shareable = resource.shareable(); //return false
 * String mappedName = resource.mappedName(); //returns &quot;&quot; because it is the
 * default value
 * Class&lt;? extends Annotation&gt; type = resource.annotationType(); //return
 * javax.annotation.Resource class
 * </pre>
 * <p/>
 * There are another ways to mock annotations:
 * <p/>
 * <pre>
 * // if you don't need to specify any property
 * Resource resource2 = mock(annotation(Resource.class));
 *
 * // if the anonymous class looks bad to you
 * AnnotationMock&lt;Resource&gt; builder = annotation(Resource.class);
 * Resource resource3 = builder.annotation();
 * builder.map(&quot;name&quot;).to(resource3.name());
 * builder.map(false).to(resource3.shareable());
 * builder.mock(); //don't forget to call mock() to activate the mock object
 * </pre>
 *
 * @param <T> The annotation type.
 *
 * @author Marcelo Guimarães
 * @since 2.1
 */
public class AnnotationMock<T extends Annotation> implements MockBuilder<T> {

  private final Class<T> annotationType;
  /** The annotation for specifying the mappings. */
  protected T annotation;
  private Map<String, Object> mappings;
  private boolean mocked;
  private String lastCall;

  /** @param annotationType the annotation type */
  public AnnotationMock(Class<T> annotationType) {
    this.annotationType = annotationType;
    initialize();
  }

  /**
   * Uses the generic parameter for defining the annotation type. So, be sure to specify
   * it.
   */
  protected AnnotationMock() {
    this.annotationType = reflect().genericType("T").in(this);
    initialize();
  }

  private void initialize() {
    this.annotation = new Interceptor(new AnnotationMockInterception())
      .createProxy().implementing(annotationType);
    this.mappings = new HashMap<String, Object>(15);
    this.mappings.put("annotationType", annotationType);
  }

  /**
   * @return the annotation for specifying the mappings. After calling {@link
   *         #createMock()} it can be used as the mocked annotation.
   */
  public T annotation() {
    return annotation;
  }

  /**
   * Maps the given value to an annotation property.
   *
   * @param <E>   The value type
   * @param value the value
   *
   * @return the component for selecting the property.
   */
  public <E> Mapper<E, T> map(final E value) {
    return new Mapper<E, T>() {

      public org.atatec.trugger.util.mock.AnnotationMock<T> to(E expected) {
        mappings.put(lastCall, value);
        return org.atatec.trugger.util.mock.AnnotationMock.this;
      }
    };
  }

  @Override
  public T createMock() {
    Predicate<Member> predicate = null;
    for (String name : mappings.keySet()) {
      predicate = (predicate == null ? named(name) : predicate.or(named(name)));
    }
    Predicate<Member> unused = predicate.negate();
    Set<Method> methods = reflect(unused)
      .methods()
      .withoutParameters()
      .in(annotationType);
    for (Method method : methods) {
      Object defaultValue = method.getDefaultValue();
      if (defaultValue == null) {
        throw new IllegalStateException("Property " + method.getName() + " not defined.");
      }
      mappings.put(method.getName(), defaultValue);
    }
    mocked = true;
    lastCall = null;
    return annotation;
  }

  /**
   * Interface for defining the annotation property .
   *
   * @param <E> The value type.
   * @param <T> The annotation type.
   *
   * @author Marcelo Guimarães
   * @since 2.1
   */
  public static interface Mapper<E, T extends Annotation> {

    /**
     * Selects the property to map the previous defined value.
     *
     * @return a reference to the builder.
     */
    org.atatec.trugger.util.mock.AnnotationMock<T> to(E value);
  }

  private class AnnotationMockInterception implements Interception {

    @Override
    public Object intercept(InterceptionContext context) throws Throwable {
      Method method = context.method();
      String name = method.getName();
      if (!mocked) {
        lastCall = name;
      }
      return mappings.containsKey(name) ? mappings.get(name) : context.nullReturn();
    }

  }

}
