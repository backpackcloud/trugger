package org.atatec.trugger.test.element;

import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.Elements;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Marcelo Guimarães
 */
public interface ElementSpecs {

  default Consumer<Element> valueIsSetTo(Object value) {
    return (element) -> element.set(value);
  }

  default Consumer<Element> valueIsSetTo(Object value, Object target) {
    return (element) -> element.in(target).set(value);
  }

  default Predicate<Element> aValue() {
    return (element) -> element.value() != null;
  }

  default Consumer<Element> attempToChangeValue() {
    return (element) -> element.set("a value");
  }

  default Consumer<Element> settingValueTo(Object value) {
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

  default Function<Element, ?> valueIn(Object target) {
    return (element) -> element.in(target).value();
  }

  default Function<Element, ?> declaringClass() {
    return (element) -> element.declaringClass();
  }

  default Function<Element, ?> valueOf(String elementName) {
    return (element) -> Elements.element(elementName).in(element.value()).value();
  }

  default Function<List, ?> elementAt(int index) {
    return list -> ((Element) list.get(index)).value();
  }

  default Consumer<Element> settingValueTo(Object value, Object target) {
    return (element) -> element.in(target).set(value);
  }

  default Consumer<Element> gettingValue() {
    return (element) -> element.value();
  }

  default Consumer<Element> gettingValueIn(Object target) {
    return (element) -> element.in(target).value();
  }

  default Predicate<Collection<Element>> elementsNamed(String... names) {
    return (collection) -> {
      Set<String> elNames = new HashSet<String>(collection.size());
      for (Element el : collection) {
        elNames.add(el.name());
      }
      if( names.length != elNames.size()) {
        return false;
      }
      for (String name : names) {
        if (!elNames.contains(name)) {
          return false;
        }
      }
      return true;
    };
  }

  default Function elementNamed(String name) {
    return obj -> Elements.element(name).in(obj);
  }

}
