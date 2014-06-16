package org.atatec.trugger.test;

import org.atatec.trugger.reflection.Reflection;
import org.mockito.stubbing.Answer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Marcelo Guimarães
 */
public class AnnotationMock {

  private static final Answer returnType(Class annotationType) {
    return (i) -> annotationType;
  }

  public static <T extends Annotation> T mockAnnotation(Class<T> annotationType) {
    T annotation = mock(annotationType);
    when(annotation.annotationType()).then(returnType(annotationType));
    List<Method> methods = Reflection.reflect().methods()
        .filter(method -> method.getDefaultValue() != null)
        .in(annotationType);
    // maps the methods with default value
    for (Method method : methods) {
      when(Reflection.invoke(method).in(annotation).withoutArgs())
          .thenReturn(method.getDefaultValue());
    }
    return annotation;
  }

}
