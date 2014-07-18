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

import tools.devnull.trugger.scan.ClassScannerFactory;
import tools.devnull.trugger.scan.ResourceFinder;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Marcelo Guimarães
 */
public class TruggerScanner implements Scanner {

  private static final String CLASS_EXTENSION = ".class";
  private static final Pattern SLASH_PATTERN = Pattern.compile("/");

  private final ClassScannerFactory factory;
  private final ClassLoader classLoader;

  public TruggerScanner(ClassScannerFactory factory, ClassLoader classLoader) {
    this.factory = factory;
    this.classLoader = classLoader;
  }

  @Override
  public List<Class> scan(String packageName) throws IOException,
      ClassNotFoundException {
    return scan(packageName, false);
  }

  @Override
  public List<Class> deepScan(String packageName) throws IOException,
      ClassNotFoundException {
    return scan(packageName, true);
  }

  private List<Class> scan(String packageName, boolean deepScan)
      throws IOException, ClassNotFoundException {
    String path = packageName.replace('.', '/');
    Enumeration<URL> resources = classLoader.getResources(path);
    List<Class> classes = new ArrayList<>(40);
    if (resources.hasMoreElements()) {
      while (resources.hasMoreElements()) {
        URL resource = resources.nextElement();
        String protocol = resource.getProtocol();
        ResourceFinder finder = factory.finderFor(protocol);
        List<String> resourcesName;
        if (deepScan) {
          resourcesName = finder.deepFind(resource, packageName);
        } else {
          resourcesName = finder.find(resource, packageName);
        }
        for (String resourceName : resourcesName) {
          if (resourceName.endsWith(CLASS_EXTENSION)) {
            resourceName =
                SLASH_PATTERN.matcher(resourceName).replaceAll(".")
                    .substring(0,
                        resourceName.length() - CLASS_EXTENSION.length());
            classes.add(Class.forName(resourceName, true, classLoader));
          }
        }
      }
    }
    return classes;
  }
}
