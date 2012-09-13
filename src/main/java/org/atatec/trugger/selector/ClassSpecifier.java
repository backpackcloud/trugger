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
package org.atatec.trugger.selector;

import org.atatec.trugger.scan.ScanLevel;

/**
 * Base interface for selecting Class objects.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.5
 */
public interface ClassSpecifier extends PredicateSelector<Class>, AnnotatedElementSelector,
  RecursionSelector {

  /**
   * Selects only instantiable classes. Classes that applies must:
   * <p/>
   * <ul> <li>not be abstract</li> <li>be public</li> <li>have at least one public
   * constructor</li> </ul>
   *
   * @since 4.1
   */
  ClassSpecifier instantiable();

  /**
   * Search recursively in the packages (sets the scan level to {@link
   * ScanLevel#SUBPACKAGES}). Is a common way to scan packages and its subpackages but
   * only take effect if using without specifying the scan level.
   */
  ClassSpecifier recursively();

  /**
   * Selects the classes that are assignable to the specified class.
   *
   * @param type the class that must be assignable from the found classes.
   *
   * @return a reference to this object.
   */
  ClassSpecifier assignableTo(Class type);

}
