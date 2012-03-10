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
package net.sf.trugger.iteration.impl;

import java.util.Collection;
import java.util.Iterator;

import net.sf.trugger.iteration.IterationSourceSelector;
import net.sf.trugger.iteration.SrcToDestIteration;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.predicate.Predicates;
import net.sf.trugger.transformer.Transformer;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public abstract class TruggerSrcToDestIteration<To, From> implements SrcToDestIteration<From> {

  /**
   * The destination collection.
   */
  protected final Collection<To> to;
  /**
   * The transformer used in the operation.
   */
  protected final Transformer<To, From> transformer;

  /**
   * @param to
   *          destination collection
   */
  protected TruggerSrcToDestIteration(Collection<To> to) {
    this(to, new NoTransformer());
  }

  /**
   * @param transformer
   *          transformer to apply on each element.
   */
  protected TruggerSrcToDestIteration(Collection<To> to, Transformer<To, From> transformer) {
    this.transformer = transformer;
    this.to = to;
  }

  public IterationSourceSelector<From> elementsMatching(Predicate<? super From> predicate) {
    return new Selector(predicate);
  }

  public IterationSourceSelector<From> allElements() {
    return new Selector(Predicates.alwaysTrue());
  }

  /**
   * Executes the operation using the given object.
   *
   * @param iterator
   *          the iterator for allow access to remove the given object from the
   *          source collection.
   * @param e
   *          the current operated object.
   */
  abstract protected void execute(Iterator<? extends From> iterator, From e);

  /**
   * A null transformer.
   */
  private static class NoTransformer implements Transformer {

    public Object transform(Object object) {
      return object;
    }
  }

  /**
   * A class for selecting the source collection.
   */
  private class Selector implements IterationSourceSelector<From> {

    private final Predicate<? super From> predicate;

    private Selector(Predicate<? super From> predicate) {
      super();
      this.predicate = predicate;
    }

    public int from(Collection<From> collection) {
      return from(collection.iterator());
    }

    public int from(Iterator<From> iterator) {
      int result = 0;
      while (iterator.hasNext()) {
        From e = iterator.next();
        if (predicate.evaluate(e)) {
          result++;
          execute(iterator, e);
        }
      }
      return result;
    }

  }

}
