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

import org.atatec.trugger.iteration.IterationSourceSelector;
import org.atatec.trugger.predicate.Predicate;

import java.util.Collection;
import java.util.Iterator;

/** @author Marcelo Varella Barca Guimarães */
public class RemoveOperation implements IterationSourceSelector {

  private final Predicate predicate;

  public RemoveOperation(Predicate predicate) {
    this.predicate = predicate;
  }

  @Override
  public void from(Collection collection) {
    if(predicate == null) {
      return;
    }
    Iterator iterator = collection.iterator();
    while (iterator.hasNext()) {
      Object next = iterator.next();
      if(predicate.evaluate(next)) {
        iterator.remove();
      }
    }
  }

}
