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

import tools.devnull.trugger.Finder;
import tools.devnull.trugger.element.Element;

import java.util.Arrays;
import java.util.List;

/**
 * @author Marcelo Guimarães
 */
public class ListElementFinder implements Finder<Element> {

  @Override
  public Element find(String name, Object target) {
    List list = (List) target;
    if ("first".equals(name)) {
      return new ListElement(list, 0);
    } else if ("last".equals(name)) {
      return new ListElement(list, list.size() - 1);
    }
    return new ListElement(list, Integer.parseInt(name));
  }

  @Override
  public List<Element> findAll(Object target) {
    List list = (List) target;
    int size = list.size();
    Element[] result = new Element[size];
    for (int i = 0; i < size; i++) {
      result[i] = new ListElement(list, i);
    }
    return Arrays.asList(result);
  }

}
