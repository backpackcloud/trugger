/*
 * Copyright 2009-2011 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.scan.impl;

import net.sf.trugger.scan.ResourceFinder;
import net.sf.trugger.scan.ScanLevel;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * A class capable of search classes in a <tt>jar</tt> resource.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class JarResourceFinder implements ResourceFinder {

  /**
   * The protocol that this finder should be registered.
   */
  public static final String PROTOCOL = "jar";
  private static final Pattern DOT_PATTERN = Pattern.compile("\\.");

  public Set<String> find(URL resource, String packageName, ScanLevel scanLevel) throws IOException {
    Set<String> resources = new HashSet<String>(30);
    findInJar(resources, resource, packageName, scanLevel);
    return resources;
  }

  private void findInJar(Set<String> resources, URL resource, String packageName, ScanLevel scanLevel) throws IOException {
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
        if (canInclude(scanLevel, resourceName, packageName)) {
          resources.add(resourceName);
        }
      }
    }
  }

  /**
   * Indicates if the specified resource can be included in the found
   * resources.
   *
   * @param resourcePath
   *            the path of the resource.
   * @param packagePath
   *            the package path that is scanned for resources.
   * @return <code>true</code> if the specified resource can be included in
   *         the found resources.
   */
  private boolean canInclude(ScanLevel scanLevel, String resourcePath, String packagePath) {
    if(scanLevel == ScanLevel.PACKAGE) {
      int start = packagePath.length() + 1;
      int end = resourcePath.indexOf('/', start);
      return resourcePath.startsWith(packagePath) && (end < 0);
    }
    return true;
  }

}
