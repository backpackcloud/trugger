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
import org.atatec.trugger.scan.ResourceFinder;
import org.atatec.trugger.scan.ScanLevel;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * A finder for zip resources (jar files) inside a JBoss AS 7.x
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 4.0
 */
public class VfsResourceFinder implements ResourceFinder {

  private static final Pattern PACKAGE_PATTERN = Pattern.compile("\\.");

  @Override
  public String protocol() {
    return "vfs";
  }

  @Override
  public Set<String> find(URL resource, String packageName, ScanLevel scanLevel) {
    try {
      VirtualFile parent = VFS.getChild(resource.toURI());
      String packagePath = PACKAGE_PATTERN.matcher(packageName).replaceAll("/");
      List<VirtualFile> resources;
      switch (scanLevel) {
        case PACKAGE:
          resources = parent.getChildren();
          break;
        case SUBPACKAGES:
          resources = parent.getChildrenRecursively();
          break;
        default:
          throw new ClassScanningException("Unknow scan level: %s", scanLevel);
      }
      Set<String> result = new HashSet<String>(resources.size());
      for (VirtualFile child : resources) {
        result.add(String.format("%s/%s", packagePath, child.getPathNameRelativeTo(parent)));
      }
      return result;
    } catch (Exception e) {
      throw new ClassScanningException(e);
    }
  }

}
