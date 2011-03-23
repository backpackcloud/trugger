/*
 * Copyright 2009-2011 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.iteration.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.trugger.iteration.Iteration;
import net.sf.trugger.iteration.IterationSearchOperation;
import net.sf.trugger.iteration.SearchException;
import net.sf.trugger.predicate.Predicate;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerSearchIteration<E> implements IterationSearchOperation<E> {

  private final Iterator<E> iterator;

  public TruggerSearchIteration(Iterator<E> iterator) {
    this.iterator = iterator;
  }

  public E elementMatching(Predicate<? super E> predicate) {
    E result = null;
    E next = null;
    boolean found = false;
    while (iterator.hasNext()) {
      next = iterator.next();
      if (predicate.evaluate(next)) {
        if (found) {
          throw new SearchException("There is more than one element that matches with the given predicate.");
        }
        found = true;
        result = next;
      }
    }
    return result;
  }

  public E firstElementMatching(Predicate<? super E> predicate) {
    E next = null;
    while (iterator.hasNext()) {
      next = iterator.next();
      if (predicate.evaluate(next)) {
        return next;
      }
    }
    return null;
  }

  public List<E> elementsMatching(Predicate<? super E> predicate) {
    List<E> result = new ArrayList<E>();
    Iteration.copyTo(result).elementsMatching(predicate).from(iterator);
    return result;
  }
}
