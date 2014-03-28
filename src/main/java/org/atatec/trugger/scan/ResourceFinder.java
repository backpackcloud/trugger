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
package org.atatec.trugger.scan;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Interface that defines a class capable of find resources in a given
 * {@link URL path} based on a package name.
 *
 * @author Marcelo Guimarães
 * @since 2.3
 */
public interface ResourceFinder {

  /**
   * @return the protocol handled by this finder
   */
  String protocol();

  /**
   * Returns a set of resource names present in the specified package on the
   * given path.
   *
   * @param path
   *            the path to the resource.
   * @param packageName
   *            the package name to search for another resources.
   * @return a list of founded resources.
   */
  List<String> find(URL path, String packageName) throws IOException;

  /**
   * Returns a set of resource names present in the specified package and its
   * subpackages on the given path.
   *
   * @param path
   *            the path to the resource.
   * @param packageName
   *            the package name to search for another resources.
   * @return a list of founded resources.
   */
  List<String> deepFind(URL path, String packageName) throws IOException;

}
