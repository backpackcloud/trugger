/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

package tools.devnull.trugger;

import tools.devnull.trugger.reflection.Reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Marcelo "Ataxexe" Guimarães
 */
public class AnnotationMock {

  public static <T extends Annotation> T mockAnnotation(Class<T> annotationType) {
    T annotation = mock(annotationType);
    when(annotation.annotationType()).then(invocation -> annotationType);
    List<Method> methods = Reflection.reflect().methods()
        .filter(method -> method.getDefaultValue() != null)
        .from(annotationType);
    // maps the methods with default value
    methods.forEach(method ->
        when(Reflection.invoke(method).on(annotation).withoutArgs())
            .thenReturn(method.getDefaultValue()));
    return annotation;
  }

}
