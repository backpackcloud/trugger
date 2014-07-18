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
package tools.devnull.trugger.scan.impl;

import tools.devnull.trugger.scan.ResourceFinder;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * A class capable of search classes in a <tt>jar</tt> resource.
 *
 * @author Marcelo Guimarães
 */
public class JarResourceFinder implements ResourceFinder {

  private static final Pattern DOT_PATTERN = Pattern.compile("\\.");

  @Override
  public String protocol() {
    return "jar";
  }

  public List<String> find(URL resource, String packageName) throws IOException {
    List<String> resources = new ArrayList<>(30);
    findInJar(resources, resource, packageName, false);
    return resources;
  }

  public List<String> deepFind(URL resource, String packageName) throws
      IOException {
    List<String> resources = new ArrayList<>(30);
    findInJar(resources, resource, packageName, true);
    return resources;
  }

  private void findInJar(List<String> resources, URL resource,
                         String packageName, boolean deepFind)
      throws IOException {
    packageName = DOT_PATTERN.matcher(packageName).replaceAll("/");
    JarURLConnection conn = (JarURLConnection) resource.openConnection();
    JarFile jarFile = conn.getJarFile();
    Enumeration<JarEntry> entries = jarFile.entries();

    while (entries.hasMoreElements()) {
      JarEntry entry = entries.nextElement();
      if (entry.isDirectory()) {
        continue;
      }
      String resourceName = entry.getName();
      if (resourceName.startsWith(packageName)) {
        if (canInclude(deepFind, resourceName, packageName)) {
          resources.add(resourceName);
        }
      }
    }
  }

  /**
   * Indicates if the specified resource can be included in the found
   * resources.
   *
   * @return <code>true</code> if the specified resource can be included in
   * the found resources.
   */
  private boolean canInclude(boolean deepFind, String resourcePath,
                             String packagePath) {
    if (!deepFind) {
      int start = packagePath.length() + 1;
      int end = resourcePath.indexOf('/', start);
      return resourcePath.startsWith(packagePath) && (end < 0);
    }
    return true;
  }

}
