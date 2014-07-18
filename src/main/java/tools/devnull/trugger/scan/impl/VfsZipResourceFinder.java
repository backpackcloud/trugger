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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A finder for zip resources (jar files) inside a JBoss AS 5.x and 6.x
 *
 * @author Marcelo Guimarães
 * @since 2.8
 */
public class VfsZipResourceFinder implements ResourceFinder {

  private static final Pattern VSFZIP_PATTERN = Pattern.compile("vfszip");

  private static final Pattern PACKAGE_PATTERN = Pattern.compile("\\.");

  private final ResourceFinder jarResourceFinder = new JarResourceFinder();

  @Override
  public String protocol() {
    return "vfszip";
  }

  @Override
  public List<String> find(URL path, String packageName) throws IOException {
    return find(path, packageName, false);
  }

  @Override
  public List<String> deepFind(URL path, String packageName) throws IOException {
    return find(path, packageName, true);
  }

  public List<String> find(URL resource, String packageName, boolean deepFind) {
    try {
      String url = VSFZIP_PATTERN.matcher(resource.toString()).replaceFirst("jar:file");
      String path = '/' + PACKAGE_PATTERN.matcher(packageName).replaceAll("/");
      url = url.replaceFirst(path, '!' + path);
      URL dirUrl = new URL(url);
      if (deepFind) {
        return jarResourceFinder.deepFind(dirUrl, packageName);
      } else {
        return jarResourceFinder.find(dirUrl, packageName);
      }
    } catch (Exception e) {
      throw new ClassScanningException(e);
    }
  }

}
