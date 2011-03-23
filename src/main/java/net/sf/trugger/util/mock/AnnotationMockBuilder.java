/*
 * Copyright 2009-2011 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.util.mock;

import net.sf.trugger.interception.Interceptor;
import net.sf.trugger.predicate.CompositePredicate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static net.sf.trugger.reflection.Reflection.invoke;
import static net.sf.trugger.reflection.Reflection.methods;
import static net.sf.trugger.reflection.Reflection.reflect;
import static net.sf.trugger.reflection.ReflectionPredicates.named;
import static net.sf.trugger.reflection.ReflectionPredicates.withDefaultValue;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;

/**
 * A builder for creating mock {@link Annotation annotations}.
 * <p>
 * This mock provides some usefull features because annotations may have default
 * values. This class can resolve them and automatically configure the values in
 * the mock object.
 * <p>
 * Here is an example of use:
 *
 * <pre>
 * import static net.sf.trugger.util.mock.Mock.annotation;
 * import static net.sf.trugger.util.mock.Mock.mock;
 * import javax.annotation.Resource;
 * // ...
 * Resource resource = new AnnotationMockBuilder&lt;Resource&gt;(){{
 *       map(&quot;name&quot;).to(annotation.name());
 *       map(false).to(annotation.shareable());
 *     }}.mock();
 * String name = resource.name(); //returns &quot;name&quot;
 * boolean shareable = resource.shareable(); //return false
 * String mappedName = resource.mappedName(); //returns &quot;&quot; because it is the default value
 * Class&lt;? extends Annotation&gt; type = resource.annotationType(); //return javax.annotation.Resource class
 * </pre>
 *
 * There are another ways to mock annotations:
 *
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
 * @author Marcelo Varella Barca Guimarães
 * @param <T>
 *          The annotation type.
 * @since 2.1
 */
public class AnnotationMockBuilder<T extends Annotation> implements MockBuilder<T> {

  private final Class<T> annotationType;
  /**
   * The annotation for specifying the mappings.
   */
  protected T annotation; //serves as a delegate to mockedAnnotation
  private T mockedAnnotation;
  private Set<String> defined;
  private boolean mocked;

  /**
   * @param annotationType
   *          the annotation type
   */
  public AnnotationMockBuilder(Class<T> annotationType) {
    this.annotationType = annotationType;
    initialize();
  }

  /**
   * Uses the generic parameter for defining the annotation type. So, be sure to
   * specify it.
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
   * @return the annotation for specifying the mappings. After calling
   *         {@link #mock()} it can be used as the mocked annotation.
   */
  public T annotation() {
    return annotation;
  }

  /**
   * Maps the given value to an annotation property.
   *
   * @param <E>
   *          The value type
   * @param value
   *          the value
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
    if(defined.isEmpty()) {
      methods = methods()
        .withoutParameters()
        .thatMatches(withDefaultValue())
      .in(annotationType);
    } else {
      String[] names = defined.toArray(new String[defined.size()]);
      CompositePredicate<Member> predicate = named(names[0]);
      for (int i = 1 ; i < names.length ; i++) {
        String name = names[i];
        predicate = predicate.or(named(name));
      }
      CompositePredicate<Member> unused = predicate.negate();
      methods = methods()
        .withoutParameters()
        .thatMatches(
          withDefaultValue()
          .and(unused))
      .in(annotationType);
    }

    for (Method method : methods) {
      invoke(method).in(mockedAnnotation).withoutArgs();
      expectLastCall().andReturn(method.getDefaultValue()).anyTimes();
    }
    replay(mockedAnnotation);
    mocked = true;
    return mockedAnnotation;
  }

  /**
   * Interface for defining the annotation property .
   *
   * @author Marcelo Varella Barca Guimarães
   * @param <E>
   *          The value type.
   * @param <T>
   *          The annotation type.
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
      return invoke(method).in(mockedAnnotation).withoutArgs();
    }

  }

}
