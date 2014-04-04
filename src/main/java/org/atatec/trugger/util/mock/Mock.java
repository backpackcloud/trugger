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
package org.atatec.trugger.util.mock;

import java.lang.annotation.Annotation;

/**
 * A helper class for creating mock objects of this framework. Use this class to
 * improve code readability.
 * <p>
 * Example:
 * <p>
 * <pre>
 * import static org.atatec.trugger.util.mock.Mock.*;
 * // ...
 *
 * Resource mock = mock(annotation(Resource.class);
 * </pre>
 *
 * @author Marcelo Guimarães
 * @since 2.0
 */
public class Mock {

  private Mock() {

  }

  /**
   * Builds a new mock using the given builder.
   *
   * @param builder the builder to create the mock.
   * @param <T>     the mock object type
   * @return the created mock object.
   */
  public static <T> T mock(MockBuilder<T> builder) {
    return builder.createMock();
  }

  /**
   * @param annotationType the annotation type
   * @return a new mock builder for {@link Annotation}.
   * @since 2.1
   */
  public static <T extends Annotation> AnnotationMock<T> annotation(Class<T> annotationType) {
    return new AnnotationMock<T>(annotationType);
  }

}
