/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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

import java.util.Collection;

import net.sf.trugger.Transformer;
import net.sf.trugger.iteration.IterationSourceSelector;
import net.sf.trugger.iteration.SrcToDestIteration;
import net.sf.trugger.iteration.TransformingIteration;
import net.sf.trugger.predicate.Predicate;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public abstract class TruggerTransformingIteration<E> implements TransformingIteration<E> {

  private final Collection to;

  /**
   * @param to
   *          destination collection.
   */
  protected TruggerTransformingIteration(Collection to) {
    this.to = to;
  }

  /**
   * Creates the operator for using without a transform.
   *
   * @param to
   *          the destination collection.
   * @return the created object.
   */
  protected abstract SrcToDestIteration<E> create(Collection to);

  /**
   * Creates the operator for using with a transform.
   *
   * @param <From>
   *          The elements type of the source collection.
   * @param to
   *          the destination collection.
   * @param transformer
   *          the transformer to use.
   * @return the created object.
   */
  protected abstract <From> SrcToDestIteration<From> create(Collection to, Transformer<E, From> transformer);

  public IterationSourceSelector<E> elementsMatching(Predicate<? super E> predicate) {
    return create(to).elementsMatching(predicate);
  }

  public IterationSourceSelector<E> allElements() {
    return create(to).allElements();
  }

  public <From> SrcToDestIteration<From> transformingWith(Transformer<E, From> transformer) {
    return create(to, transformer);
  }

}
