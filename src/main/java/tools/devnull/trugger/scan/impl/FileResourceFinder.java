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

import tools.devnull.trugger.scan.ClassScanningException;
import tools.devnull.trugger.scan.ResourceFinder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A class capable of search classes in a <tt>file/directory</tt> resource.
 *
 * @author Marcelo Guimarães
 */
public class FileResourceFinder implements ResourceFinder {

  private static final Pattern DOT_PATTERN = Pattern.compile("\\.");

  @Override
  public String protocol() {
    return "file";
  }

  public List<String> find(URL resource, String packageName) {
    List<String> resources = new ArrayList<>(30);
    String packagePath = DOT_PATTERN.matcher(packageName).replaceAll("/");
    findInDirectory(resources, resource, packagePath, false);
    return resources;
  }

  public List<String> deepFind(URL resource, String packageName) {
    List<String> resources = new ArrayList<>(30);
    findInDirectory(resources, resource, packageName, true);
    return resources;
  }

  private void findInDirectory(List<String> resources, URL fullPath,
                               String packageName, boolean deepFind) {
    String packagePath = DOT_PATTERN.matcher(packageName).replaceAll("/");
    File directory;
    try {
      directory = new File(fullPath.toURI());
    } catch (URISyntaxException e) {
      directory = new File(fullPath.getPath());
    }
    File[] files = directory.listFiles();
    if (files != null) {
      for (File file : files) {
        if (canInclude(deepFind, file)) {
          if (file.isDirectory()) {
            try {
              findInDirectory(resources, file.toURI().toURL(),
                  packagePath + '/' + file.getName(), deepFind);
            } catch (MalformedURLException e) {
              throw new ClassScanningException(e);
            }
          } else {
            String simpleName = file.getName();
            resources.add(String.format("%s/%s", packagePath, simpleName));
          }
        }
      }
    }
  }

  /**
   * Indicates if the specified resource can be included in the found resources.
   *
   * @return <code>true</code> if the specified resource can be included in the
   * found resources.
   */
  private static boolean canInclude(boolean deepFind, File resourceFile) {
    return deepFind || !resourceFile.isDirectory();
  }

}
