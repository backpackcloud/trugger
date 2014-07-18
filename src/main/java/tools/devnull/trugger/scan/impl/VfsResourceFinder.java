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
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A finder for zip resources (jar files) inside a JBoss AS 7.x
 *
 * @author Marcelo Guimarães
 * @since 4.0
 */
public class VfsResourceFinder implements ResourceFinder {

  private static final Pattern PACKAGE_PATTERN = Pattern.compile("\\.");

  @Override
  public String protocol() {
    return "vfs";
  }

  @Override
  public List<String> find(URL path, String packageName) throws IOException {
    return find(path, packageName, false);
  }

  @Override
  public List<String> deepFind(URL path, String packageName) throws IOException {
    return find(path, packageName, true);
  }

  private List<String> find(URL resource, String packageName,
                            boolean deepFind) {
    try {
      VirtualFile parent = VFS.getChild(resource.toURI());
      String packagePath = PACKAGE_PATTERN.matcher(packageName).replaceAll("/");
      List<VirtualFile> resources;
      if (deepFind) {
        resources = parent.getChildrenRecursively();
      } else {
        resources = parent.getChildren();
      }
      List<String> result = new ArrayList<>(resources.size());
      for (VirtualFile child : resources) {
        result.add(String.format("%s/%s",
                packagePath,
                child.getPathNameRelativeTo(parent))
        );
      }
      return result;
    } catch (Exception e) {
      throw new ClassScanningException(e);
    }
  }

}
