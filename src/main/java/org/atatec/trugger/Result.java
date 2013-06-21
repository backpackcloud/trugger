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
 * Interface that defines the final state of a request language. In this state,
 * the request result is returned based on the configurations provided before
 * plus the target specified at this time.
 * 
 * @author Marcelo Guimarães
 * @param <Value>
 *          the result object type
 * @param <Target>
 *          the target type
 */
public interface Result<Value, Target> {
  
  /**
   * Computes the request based on the given target and previous configurations.
   * 
   * @param target
   *          the target for compute the request.
   * @return the result.
   */
  Value in(Target target);
  
}
