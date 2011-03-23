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

import java.util.Collection;
import java.util.Iterator;

import net.sf.trugger.transformer.Transformer;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public final class TruggerMoveIteration<To, From> extends TruggerSrcToDestIteration<To, From> {

  /**
   * @param to
   *          destination collection
   * @param transformer
   *          transformer to apply on each element.
   */
  public TruggerMoveIteration(Collection<To> to, Transformer<To, From> transformer) {
    super(to, transformer);
  }

  /**
   * @param to
   *          destination collection
   */
  public TruggerMoveIteration(Collection<To> to) {
    super(to);
  }

  @Override
  protected void execute(Iterator<? extends From> iterator, From e) {
    to.add(transformer.transform(e));
    iterator.remove();
  }

}
