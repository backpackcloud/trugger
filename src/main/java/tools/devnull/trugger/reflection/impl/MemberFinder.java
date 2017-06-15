/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.devnull.trugger.reflection.impl;

import java.lang.reflect.Member;

/**
 * Interface that defines a class capable of find {@link Member} objects.
 * 
 * @author Marcelo Guimarães
 */
public interface MemberFinder<T extends Member> {
  
  /**
   * Finds the object in the specified type.
   * 
   * @param type
   *          the type for searching the object.
   * @return the found object.
   * @throws Exception
   *           if an error occurs.
   */
  T find(Class<?> type) throws Exception;
  
}
