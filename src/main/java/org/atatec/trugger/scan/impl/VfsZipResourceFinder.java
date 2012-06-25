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
package org.atatec.trugger.scan.impl;

import org.atatec.trugger.scan.ClassScanningException;
import org.atatec.trugger.scan.ScanLevel;

import java.net.URL;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * A finder for zip resources (jar files) inside a JBoss AS 5.x and 6.x
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.8
 */
public class VfsZipResourceFinder extends JarResourceFinder {

  /**
   * The protocol that this finder should be registered.
   */
  public static final String PROTOCOL = "vfszip";
  private static final Pattern VSFZIP_PATTERN = Pattern.compile("vfszip");
  private static final Pattern PACKAGE_PATTERN = Pattern.compile("\\.");

  @Override
  public Set<String> find(URL resource, String packageName, ScanLevel scanLevel) {
    try {
      String url = VSFZIP_PATTERN.matcher(resource.toString()).replaceFirst("jar:file");
      String path = '/' + PACKAGE_PATTERN.matcher(packageName).replaceAll("/");
      url = url.replaceFirst(path, '!' + path);
      URL dirUrl = new URL(url);
      return super.find(dirUrl, packageName, scanLevel);
    } catch (Exception e) {
      throw new ClassScanningException(e);
    }
  }

}
