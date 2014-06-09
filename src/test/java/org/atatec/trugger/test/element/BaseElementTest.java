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

  default Consumer<Element> valueIsSetTo(Object value, Object target) {
    return (element) -> element.in(target).set(value);
  }

  default Consumer<Element> shouldHaveAValue() {
    return (element) -> assertNotNull(element.value());
  }

  default Consumer<Element> attempToChangeValue() {
    return (element) -> element.set("a value");
  }

  default Consumer<Element> attempToSetValueTo(Object value) {
    return (element) -> element.set(value);
  }

  default Consumer<Element> attempToGetValue() {
    return (element) -> element.value();
  }

  default Function<Element, ?> type() {
    return (element) -> element.type();
  }

  default Function<Element, ?> name() {
    return (element) -> element.name();
  }

  default Function<Element, ?> value() {
    return (element) -> element.value();
  }

  default Function<Element, ?> specific() {
    return (element) -> element.isSpecific();
  }

  default Function<Element, ?> valueIn(Object target) {
    return (element) -> element.in(target).value();
  }

  default Function<Element, ?> declaringClass() {
    return (element) -> element.declaringClass();
  }

}
