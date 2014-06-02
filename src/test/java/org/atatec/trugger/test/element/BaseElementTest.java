package org.atatec.trugger.test.element;

import org.atatec.trugger.element.Element;

import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.Assert.assertNotNull;

/**
 * @author Marcelo Guimarães
 */
public interface BaseElementTest {

  default Consumer<Element> valueIsSetTo(Object value) {
    return (element) -> element.set(value);
  }

  default Consumer<Element> shouldHaveAValue() {
    return (element) -> assertNotNull(element.value());
  }

  default Consumer<Element> attempToChangeValue() {
    return (element) -> element.set("a value");
  }

  default Consumer<Element> attempToGetValue() {
    return (element) -> element.value();
  }

  default Function<Element, ?> type() {
    return (element) -> element.type();
  }

  default Function<Element, ?> value() {
    return (element) -> element.value();
  }

  default Function<Element, ?> declaringClass() {
    return (element) -> element.declaringClass();
  }

}
