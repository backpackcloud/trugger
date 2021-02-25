/*
 * The Apache License
 *
 * Copyright 2009 Marcelo Guimaraes <ataxexe@backpackcloud.com>
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

package com.backpackcloud.trugger.element;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Marcelo Guimaraes
 */
public interface ElementExpectations {

  default Function<Element, String> stringRepresentation() {
    return el -> el.toString();
  }

  default Consumer<Element> valueIsSetTo(Object value) {
    return (element) -> element.setValue(value);
  }

  default Consumer<Element> valueIsSetTo(Object value, Object target) {
    return (element) -> element.on(target).setValue(value);
  }

  default Predicate<Element> aValue() {
    return (element) -> element.getValue() != null;
  }

  default Consumer<Element> attempToChangeValue() {
    return (element) -> element.setValue("a value");
  }

  default Consumer<Element> settingValueTo(Object value) {
    return (element) -> element.setValue(value);
  }

  default Consumer<Element> getValue() {
    return Element::getValue;
  }

  default Function<Element, ?> valueIn(Object target) {
    return (element) -> element.on(target).getValue();
  }

  default Function<Element, ?> valueOf(String elementName) {
    return (element) -> Elements.element(elementName).from(element.getValue()).get().getValue();
  }

  default Function<List, ?> elementAt(int index) {
    return list -> ((Element) list.get(index)).getValue();
  }

  default Consumer<Element> settingValueTo(Object value, Object target) {
    return (element) -> element.on(target).setValue(value);
  }

  default Consumer<Element> gettingValue() {
    return (element) -> element.getValue();
  }

  default Consumer<Element> gettingValueIn(Object target) {
    return (element) -> element.on(target).getValue();
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
    return obj -> Elements.element(name).from(obj).get();
  }

}
