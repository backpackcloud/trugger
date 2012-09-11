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
package org.atatec.trugger.util.mock;

import org.atatec.trugger.interception.Interceptor;
import org.atatec.trugger.predicate.CompositePredicate;
import org.atatec.trugger.reflection.Reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.atatec.trugger.reflection.Reflection.methods;
import static org.atatec.trugger.reflection.Reflection.reflect;
import static org.atatec.trugger.reflection.MethodPredicates.HAS_DEFAULT_VALUE;
import static org.atatec.trugger.reflection.ReflectionPredicates.named;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;

/**
 * A builder for creating mock {@link Annotation annotations}.
 * <p/>
 * This mock provides some usefull features because annotations may have default values.
 * This class can resolve them and automatically configure the values in the mock object.
 * <p/>
 * Here is an example of use:
 * <p/>
 * <pre>
 * import static org.atatec.trugger.util.mock.Mock.annotation;
 * import static org.atatec.trugger.util.mock.Mock.mock;
 * import javax.annotation.Resource;
 * // ...
 * Resource resource = new AnnotationMockBuilder&lt;Resource&gt;(){{
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
 * // if the annonymous class looks bad to you
 * AnnotationMockBuilder&lt;Resource&gt; builder = annotation(Resource.class);
 * Resource resource3 = builder.annotation();
 * builder.map(&quot;name&quot;).to(resource3.name());
 * builder.map(false).to(resource3.shareable());
 * builder.mock(); //don't forget to call mock() to activate the mock object
 * </pre>
 *
 * @param <T> The annotation type.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
public class AnnotationMockBuilder<T extends Annotation> implements MockBuilder<T> {

  private final Class<T> annotationType;
  /** The annotation for specifying the mappings. */
  protected T annotation; //serves as a delegate to mockedAnnotation
  private T mockedAnnotation;
  private Set<String> defined;
  private boolean mocked;

  /** @param annotationType the annotation type */
  public AnnotationMockBuilder(Class<T> annotationType) {
    this.annotationType = annotationType;
    initialize();
  }

  /**
   * Uses the generic parameter for defining the annotation type. So, be sure to specify
   * it.
   */
  protected AnnotationMockBuilder() {
    this.annotationType = reflect().genericType("T").in(this);
    initialize();
  }

  private void initialize() {
    this.mockedAnnotation = createMock(annotationType);
    this.annotation = (T) new ConfigurationInterceptor().createProxy().implementing(annotationType).withoutTarget();
    this.defined = new HashSet<String>();
    expect((Class) mockedAnnotation.annotationType()).andReturn(annotationType).anyTimes();
  }

  /**
   * @return the annotation for specifying the mappings. After calling {@link #mock()} it
   *         can be used as the mocked annotation.
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

      public AnnotationMockBuilder<T> to(E expected) {
        expectLastCall().andReturn(value).anyTimes();
        return AnnotationMockBuilder.this;
      }
    };
  }

  @Override
  public T mock() {
    Set<Method> methods;
    if (defined.isEmpty()) {
      methods = methods()
        .withoutParameters()
        .that(HAS_DEFAULT_VALUE)
        .in(annotationType);
    } else {
      String[] names = defined.toArray(new String[defined.size()]);
      CompositePredicate<Member> predicate = named(names[0]);
      for (int i = 1; i < names.length; i++) {
        String name = names[i];
        predicate = predicate.or(named(name));
      }
      CompositePredicate<Member> isUnused = predicate.negate();
      methods = methods()
        .withoutParameters()
        .that(
          HAS_DEFAULT_VALUE
            .and(isUnused))
        .in(annotationType);
    }

    for (Method method : methods) {
      Reflection.invoke(method).in(mockedAnnotation).withoutArgs();
      expectLastCall().andReturn(method.getDefaultValue()).anyTimes();
    }
    replay(mockedAnnotation);
    mocked = true;
    return mockedAnnotation;
  }

  /**
   * Interface for defining the annotation property .
   *
   * @param <E> The value type.
   * @param <T> The annotation type.
   *
   * @author Marcelo Varella Barca Guimarães
   * @since 2.1
   */
  public static interface Mapper<E, T extends Annotation> {

    /**
     * Selects the property to map the previous defined value.
     *
     * @return a reference to the builder.
     */
    AnnotationMockBuilder<T> to(E value);
  }

  private class ConfigurationInterceptor extends Interceptor {

    @Override
    protected Object intercept() throws Throwable {
      Method method = method();
      if (!mocked) {
        defined.add(method.getName());
      }
      //delegates to the mock object
      return Reflection.invoke(method).in(mockedAnnotation).withoutArgs();
    }

  }

}
