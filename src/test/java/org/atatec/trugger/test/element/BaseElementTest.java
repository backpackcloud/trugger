package org.atatec.trugger.test.element;

import org.atatec.trugger.element.Element;

import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.Assert.assertNotNull;

/**
 * @author Marcelo Guimarães
 */
public abstract class BaseElementTest {

  protected Consumer<Element> valueIsSetTo(Object value) {
    return (element) -> element.set(value);
  }

  protected Consumer<Element> shouldHaveAValue() {
    return (element) -> assertNotNull(element.value());
  }

  protected Consumer<Element> attempToChangeValue() {
    return (element) -> element.set("a value");
  }

  protected Consumer<Element> attempToGetValue() {
    return (element) -> element.value();
  }

  protected Function<Element, ?> type() {
    return (element) -> element.type();
  }

  protected Function<Element, ?> value() {
    return (element) -> element.value();
  }

  protected Function<Element, ?> declaringClass() {
    return (element) -> element.declaringClass();
  }

}
