package tools.devnull.trugger;

import tools.devnull.trugger.reflection.Reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Marcelo Guimarães
 */
public class AnnotationMock {

  public static <T extends Annotation> T mockAnnotation(Class<T> annotationType) {
    T annotation = mock(annotationType);
    when(annotation.annotationType()).then(invocation -> annotationType);
    List<Method> methods = Reflection.reflect().methods()
        .filter(method -> method.getDefaultValue() != null)
        .in(annotationType);
    // maps the methods with default value
    methods.forEach(method ->
        when(Reflection.invoke(method).in(annotation).withoutArgs())
            .thenReturn(method.getDefaultValue()));
    return annotation;
  }

}
