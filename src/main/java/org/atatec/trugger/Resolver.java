/*
 * Copyright 2009-2012 Marcelo Guimarães
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
package org.atatec.trugger;

/**
 * Interface that defines a component for resolving values against a specified
 * target.
 * 
 * @author Marcelo Guimarães
 * @param <Value>
 *          the value type
 * @param <Target>
 *          the target type
 */
public interface Resolver<Value, Target> {
  
  /**
   * Resolves the value given the specified target.
   * 
   * @param target
   *          the target
   * @return the resolved value.
   */
  Value resolve(Target target);
  
}
