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
package org.atatec.trugger.scan;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Defines the level for a package scan.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.3
 */
public enum ScanLevel {

	/**
	 * Scans only the specified package.
	 */
	PACKAGE,
	/**
	 * Scans the specified package and its subpackages.
	 */
	SUBPACKAGES;

	/**
	 * @param packageName
	 *            the package name.
	 * @return the object that represents the package scan with this scan level.
	 */
	public PackageScan createScanPackage(String packageName) {
		return new PackageScan(packageName, this);
	}

	/**
	 * @param packageNames
	 *            the names of the packages for scanning.
	 * @return a collection of objects that represents the package scan with
	 *         this scan level.
	 */
	public Collection<PackageScan> createScanPackages(String... packageNames) {
		Collection<PackageScan> collection = new ArrayList<PackageScan>();
		for (String packageName : packageNames) {
			collection.add(createScanPackage(packageName));
		}
		return collection;
	}

}
