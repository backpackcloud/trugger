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

import java.lang.annotation.Annotation;

import net.sf.trugger.Finder;
import net.sf.trugger.element.Element;

/**
 * A helper class for creating mock objects of this framework. Use this class to
 * improve code readability.
 * <p>
 * Example:
 * 
 * <pre>
 * import static net.sf.trugger.util.mock.Mock.*;
 * // ...
 * 
 * Element mock = mock(element().named(&quot;myElement&quot;).ofType(String.class).readable());
 * 
 * // that code is equivalent to this one:
 * Element mock = new ElementMockBuilder().named(&quot;myElement&quot;).ofType(String.class).readable().mock();
 * </pre>
 * 
 * @author Marcelo Varella Barca Guimarães
 * @since 2.0
 */
public class Mock {
  
  /**
   * Builds a new mock using the given builder.
   * 
   * @param builder
   *          the builder to create the mock.
   * @param <T>
   *          the mock object type
   * @return the created mock object.
   */
  public static <T> T mock(MockBuilder<T> builder) {
    return builder.mock();
  }
  
  /**
   * @return a new mock builder for {@link Element}.
   */
  public static ElementMockBuilder element() {
    return new ElementMockBuilder();
  }
  
  /**
   * @return a new mock builder for {@link Finder} of {@link Element elements}.
   */
  public static ElementFinderMockBuilder elementFinder() {
    return new ElementFinderMockBuilder();
  }
  
  /**
   * @param annotationType
   *          the annotation type
   * @return a new mock builder for {@link Annotation}.
   * @since 2.1
   */
  public static <T extends Annotation> AnnotationMockBuilder<T> annotation(Class<T> annotationType) {
    return new AnnotationMockBuilder<T>(annotationType);
  }
  
}
