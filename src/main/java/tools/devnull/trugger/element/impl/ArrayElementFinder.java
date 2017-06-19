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

package tools.devnull.trugger.element.impl;

import tools.devnull.trugger.Optional;
import tools.devnull.trugger.element.ElementFinder;
import tools.devnull.trugger.element.Element;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * @author Marcelo "Ataxexe" Guimarães
 */
public class ArrayElementFinder implements ElementFinder {

  @Override
  public Optional<Element> find(String name, Object array) {
    Element result;
    if ("first".equals(name)) {
      result = new ArrayElement(array, 0);
    } else if ("last".equals(name)) {
      result = new ArrayElement(array, Array.getLength(array) - 1);
    } else {
      result = new ArrayElement(array, Integer.parseInt(name));
    }
    return Optional.of(result);
  }

  @Override
  public List<Element> findAll(Object array) {
    int size = Array.getLength(array);
    Element[] result = new Element[size];
    for (int i = 0; i < size; i++) {
      result[i] = new ArrayElement(array, i);
    }
    return Arrays.asList(result);
  }

}
