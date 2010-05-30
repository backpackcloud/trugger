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
package net.sf.trugger.iteration;

import net.sf.trugger.transformer.Transformer;

/**
 * Interface that defines an iteration that can transform elements.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public interface TransformingIteration<To> extends SrcToDestIteration<To> {

  /**
   * Executes iteration transforming the elements using the given transformer.
   *
   * @param transformer
   *          the transformer to use.
   * @return the component for defining the elements.
   */
  <From> SrcToDestIteration<From> transformingWith(Transformer<To, From> transformer);
}
