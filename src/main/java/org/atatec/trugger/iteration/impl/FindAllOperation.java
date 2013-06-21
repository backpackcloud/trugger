/*
 * Copyright 2009-2012 Marcelo Guimarães
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

package org.atatec.trugger.iteration.impl;

import org.atatec.trugger.iteration.FindAllResult;
import org.atatec.trugger.predicate.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** @author Marcelo Guimarães */
public class FindAllOperation implements FindAllResult {

  private final Predicate predicate;

  public FindAllOperation(Predicate predicate) {
    this.predicate = predicate;
  }

  @Override
  public <E> List<E> in(Collection<E> collection) {
    List<E> result = new ArrayList<E>(collection.size());
    for (E e : collection) {
      if(predicate.evaluate(e)) {
        result.add(e);
      }
    }
    return result;
  }

}
