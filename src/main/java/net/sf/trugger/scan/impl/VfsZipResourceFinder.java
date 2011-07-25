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

import java.net.URL;
import java.util.Set;

import net.sf.trugger.scan.ClassScanningException;
import net.sf.trugger.scan.ScanLevel;

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

  @Override
  public Set<String> find(URL resource, String packageName, ScanLevel scanLevel) {
    try {
      String url = resource.toString().replaceFirst("vfszip", "jar:file");
      String path = "/" + packageName.replaceAll("\\.", "/");
      url = url.replaceFirst(path, "!" + path);
      URL dirUrl = new URL(url);
      return super.find(dirUrl, packageName, scanLevel);
    } catch (Exception e) {
      throw new ClassScanningException(e);
    }
  }

}
