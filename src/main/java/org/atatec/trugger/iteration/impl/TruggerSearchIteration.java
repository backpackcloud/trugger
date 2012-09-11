/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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

import org.atatec.trugger.iteration.Iteration;
import org.atatec.trugger.iteration.IterationSearchOperation;
import org.atatec.trugger.iteration.SearchException;
import org.atatec.trugger.predicate.Predicate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerSearchIteration<E> implements IterationSearchOperation<E> {

  private final Iterator<E> iterator;

  public TruggerSearchIteration(Iterator<E> iterator) {
    this.iterator = iterator;
  }

  public E oneThat(Predicate<? super E> predicate) {
    E result = null;
    E next;
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

  public E first(Predicate<? super E> predicate) {
    E next;
    while (iterator.hasNext()) {
      next = iterator.next();
      if (predicate.evaluate(next)) {
        return next;
      }
    }
    return null;
  }

  public List<E> anyThat(Predicate<? super E> predicate) {
    List<E> result = new ArrayList<E>();
    Iteration.copyTo(result).anyThat(predicate).from(iterator);
    return result;
  }
}
