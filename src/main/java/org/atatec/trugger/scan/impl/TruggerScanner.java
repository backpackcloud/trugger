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
package org.atatec.trugger.scan.impl;

import org.atatec.trugger.scan.ClassScannerFactory;
import org.atatec.trugger.scan.PackageScan;
import org.atatec.trugger.scan.ResourceFinder;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Marcelo Guimarães
 */
public class TruggerScanner implements Scanner {

  private static final String CLASS_EXTENSION = ".class";
  private static final Pattern SLASH_PATTERN = Pattern.compile("/");

  private final ClassScannerFactory factory;
  private ClassLoader classLoader;

  public TruggerScanner(ClassScannerFactory factory, ClassLoader classLoader) {
    this.factory = factory;
    this.classLoader = classLoader;
  }

  public void setClassLoader(ClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  /**
   * Scans and returns the found classes in the specified package.
   *
   * @param packageEntry
   *          the package to scan.
   * @return the classes found in the package
   */
  public Set<Class> scanPackage(PackageScan packageEntry) throws IOException, ClassNotFoundException {
    String path = packageEntry.packageName().replace('.', '/');
    Enumeration<URL> resources = classLoader.getResources(path);
    Set<Class> classes = new HashSet<Class>(40);
    if (resources.hasMoreElements()) {
      while (resources.hasMoreElements()) {
        URL resource = resources.nextElement();
        String protocol = resource.getProtocol();
        ResourceFinder finder = factory.finderFor(protocol);
        Set<String> resourcesName = finder.find(resource, packageEntry.packageName(), packageEntry.scanLevel());
        for (String resourceName : resourcesName) {
          if (resourceName.endsWith(CLASS_EXTENSION)) {
            resourceName =
                SLASH_PATTERN.matcher(resourceName).replaceAll(".").substring(0, resourceName.length() - CLASS_EXTENSION.length());
            classes.add(Class.forName(resourceName, true, classLoader));
          }
        }
      }
    }
    return classes;
  }

}
