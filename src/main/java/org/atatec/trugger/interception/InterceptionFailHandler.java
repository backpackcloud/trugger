/*
 * Copyright 2009-2014 Marcelo Guimarães
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

package org.atatec.trugger.interception;

/**
 * Interface that defines a handler to deal with interception fails.
 *
 * @since 5.0
 */
@FunctionalInterface
public interface InterceptionFailHandler {

  /**
   * Handles the error.
   *
   * @param context the context in which the interception failed
   * @param error   the error
   * @return a value to return for the caller
   */
  Object handle(InterceptionContext context, Throwable error) throws Throwable;

}
