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

import org.atatec.trugger.iteration.FindAllResult;
import org.atatec.trugger.iteration.IterationFactory;
import org.atatec.trugger.iteration.IterationSourceSelector;
import org.atatec.trugger.iteration.SourceSelector;
import org.atatec.trugger.predicate.Predicate;

/**
 * The default implementation for {@link IterationFactory}.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerIterationFactory implements IterationFactory {

  @Override
  public IterationSourceSelector createRetainOperation(Predicate predicate) {
    return new RetainOperation(predicate);
  }

  @Override
  public IterationSourceSelector createRemoveOperation(Predicate predicate) {
    return new RemoveOperation(predicate);
  }

  @Override
  public SourceSelector createMoveOperation(Predicate predicate) {
    return new MoveOperation(predicate);
  }

  @Override
  public SourceSelector createCopyOperation(Predicate predicate) {
    return new CopyOperation(predicate);
  }

  @Override
  public FindResult createFindOperation(Predicate predicate) {
    return new FindOperation(predicate);
  }

  @Override
  public FindResult createFindFirstOperation(Predicate predicate) {
    return new FindFirstOperation(predicate);
  }

  @Override
  public FindAllResult createFindAllOperation(Predicate predicate) {
    return new FindAllOperation(predicate);
  }

}
