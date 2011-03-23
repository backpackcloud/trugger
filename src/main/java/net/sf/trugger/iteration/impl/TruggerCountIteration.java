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

import java.util.Iterator;

import net.sf.trugger.iteration.SrcIteration;
import net.sf.trugger.predicate.Predicate;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public final class TruggerCountIteration<E> implements SrcIteration<E> {

  private final Iterator<? extends E> iterator;

  public TruggerCountIteration(Iterator<? extends E> iterator) {
    this.iterator = iterator;
  }

  public int elementsMatching(Predicate<? super E> predicate) {
    int result = 0;
    while (iterator.hasNext()) {
      E e = iterator.next();
      if (predicate.evaluate(e)) {
        result++;
      }
    }
    return result;
  }

}
