package org.atatec.trugger.test.validation;

import org.atatec.trugger.element.Element;
import org.atatec.trugger.validation.InvalidElement;
import org.atatec.trugger.validation.impl.InvalidElementImpl;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Marcelo Guimarães
 */
public class InvalidElementTest {

  private Element element = mock(Element.class);

  private Object invalidValue = new Object();

  private InvalidElement invalidElement = new InvalidElementImpl(element, invalidValue);

  @Test
  public void testInvalidValue() {
    assertSame(invalidValue, invalidElement.invalidValue());
  }

  @Test
  public void testDelegate() {
    Object value = new Object();
    Object target = new Object();

    invalidElement.target();
    verify(element).target();

    invalidElement.in(target);
    verify(element).in(target);

    invalidElement.declaringClass();
    verify(element).declaringClass();

    invalidElement.isReadable();
    verify(element).isReadable();

    invalidElement.isSpecific();
    verify(element).isSpecific();

    invalidElement.isWritable();
    verify(element).isWritable();

    invalidElement.name();
    verify(element).name();

    invalidElement.set(value);
    verify(element).set(value);

    invalidElement.type();
    verify(element).type();

    invalidElement.getAnnotations();
    verify(element).getAnnotations();

    invalidElement.getAnnotation(Test.class);
    verify(element).getAnnotation(Test.class);

    invalidElement.getDeclaredAnnotations();
    verify(element).getDeclaredAnnotations();
  }

}
