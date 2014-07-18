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
package tools.devnull.trugger.selector;

import tools.devnull.trugger.Result;
import tools.devnull.trugger.scan.ClassScanningException;

import java.util.List;
import java.util.function.Predicate;

/**
 * Interface that defines a selector for {@link Class} objects.
 *
 * @author Marcelo Guimarães
 * @since 2.3
 */
public interface ClassesSelector extends PredicateSelector<Class>,
    DeepSelector, Result<List<Class>, String> {

  ClassesSelector filter(Predicate<? super Class> predicate);

  /**
   * Scans the subpackages in addition to the given package
   *
   * @return a new selector that will scan subpackages too
   */
  ClassesSelector deep();

  /**
   * Scans the packages for classes.
   *
   * @param packageName the package to scan.
   * @return the search result.
   * @throws ClassScanningException if an error occurs while scanning the
   *                                packages.
   */
  List<Class> in(String packageName) throws ClassScanningException;

}
