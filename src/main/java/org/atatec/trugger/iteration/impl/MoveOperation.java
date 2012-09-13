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

import org.atatec.trugger.iteration.DestinationSelector;
import org.atatec.trugger.iteration.SourceSelector;
import org.atatec.trugger.predicate.Predicate;
import org.atatec.trugger.transformer.Transformer;

import java.util.Collection;
import java.util.Iterator;

/** @author Marcelo Varella Barca Guimarães */
public class MoveOperation implements SourceSelector {

  private final Predicate predicate;
  private Transformer transformer;

  public MoveOperation(Predicate predicate) {
    this.predicate = predicate;
  }

  @Override
  public SourceSelector as(Transformer transformer) {
    this.transformer = transformer;
    return this;
  }

  @Override
  public DestinationSelector from(final Collection src) {
    return new DestinationSelector() {
      @Override
      public void to(Collection dest) {
        if (predicate == null && transformer == null) {
          dest.addAll(src);
          src.clear();
        } else {
          Iterator iterator = src.iterator();
          while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (predicate.evaluate(obj)) {
              if (transformer != null) {
                obj = transformer.transform(obj);
              }
              dest.add(obj);
              iterator.remove();
            }
          }
        }
      }
    };
  }
}
