/*
 * Copyright 2009-2014 Marcelo Guimarães
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.atatec.trugger.element.impl;

import org.atatec.trugger.Finder;
import org.atatec.trugger.Result;
import org.atatec.trugger.element.Element;

import java.lang.reflect.Array;
import java.util.LinkedHashSet;
import java.util.Set;

/** @author Marcelo Guimarães */
public class ArrayElementFinder implements Finder<Element> {

  @Override
  public Result<Element, Object> find(final String name) {
    return new Result<Element, Object>() {
      @Override
      public Element in(Object array) {
        if ("first".equals(name)) {
          return new ArrayElement(array, 0);
        } else if ("last".equals(name)) {
          return new ArrayElement(array, Array.getLength(array) - 1);
        }
        return new ArrayElement(array, Integer.parseInt(name));
      }
    };
  }

  @Override
  public Result<Set<Element>, Object> findAll() {
    return new Result<Set<Element>, Object>() {
      @Override
      public Set<Element> in(Object array) {
        int size = Array.getLength(array);
        Set<Element> result = new LinkedHashSet<Element>(size);
        for (int i = 0; i < size; i++) {
          result.add(new ArrayElement(array,  i));
        }
        return result;
      }
    };
  }

}
