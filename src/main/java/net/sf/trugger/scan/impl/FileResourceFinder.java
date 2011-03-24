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

import net.sf.trugger.scan.ClassScanningException;
import net.sf.trugger.scan.ResourceFinder;
import net.sf.trugger.scan.ScanLevel;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * A class capable of search classes in a <tt>file/directory</tt> resource.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class FileResourceFinder implements ResourceFinder {

	/**
	 * The protocol that this finder should be registered.
	 */
	public static final String PROTOCOL = "file";
  private static final Pattern DOT_PATTERN = Pattern.compile("\\.");

  public Set<String> find(URL resource, String packageName, ScanLevel scanLevel) {
		Set<String> resources = new HashSet<String>(30);
		String packagePath = DOT_PATTERN.matcher(packageName).replaceAll("/");
		findInDirectory(resources, resource, packagePath, scanLevel);
		return resources;
	}

	private void findInDirectory(Set<String> resources, URL fullPath, String packagePath, ScanLevel scanLevel) {
		File directory;
    try {
      directory = new File(fullPath.toURI());
    } catch (URISyntaxException e) {
      directory = new File(fullPath.getPath());
    }
		File[] files = directory.listFiles();
		for (File file : files) {
			if (canInclude(scanLevel, file)) {
				if (file.isDirectory()) {
					try {
            findInDirectory(resources, file.toURI().toURL(), packagePath + '/' + file.getName(), scanLevel);
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

	/**
	 * Indicates if the specified resource can be included in the found
	 * resources.
	 *
	 * @param scanLevel
	 *            the scan level for the package.
	 * @param resourceFile
	 *            the file that represents this resource.
	 * @return <code>true</code> if the specified resource can be included in
	 *         the found resources.
	 */
	private static boolean canInclude(ScanLevel scanLevel, File resourceFile) {
    return scanLevel != ScanLevel.PACKAGE || !resourceFile.isDirectory();
  }

}
