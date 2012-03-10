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
package net.sf.trugger.scan;

/**
 * Class that defines a package for scanning.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.3
 */
public class PackageScan {

	private final String packageName;
	private final ScanLevel scanLevel;

	/**
	 * @param packageName
	 *            the name of the package. Must not be <code>null</code>.
	 * @param scanLevel
	 *            the level of the scan. Must not be <code>null</code>.
	 */
	public PackageScan(String packageName, ScanLevel scanLevel) {
		this.packageName = packageName;
		this.scanLevel = scanLevel;
	}

	/**
	 * @return the name of the package to scan.
	 */
	public String packageName() {
		return packageName;
	}

	/**
	 * @return the level of the scan.
	 */
	public ScanLevel scanLevel() {
		return scanLevel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((packageName == null) ? 0 : packageName.hashCode());
		result = prime * result + ((scanLevel == null) ? 0 : scanLevel.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PackageScan other = (PackageScan) obj;
		if (packageName == null) {
			if (other.packageName != null) {
				return false;
			}
		} else if (!packageName.equals(other.packageName)) {
			return false;
		}
		if (scanLevel == null) {
			if (other.scanLevel != null) {
				return false;
			}
		} else if (!scanLevel.equals(other.scanLevel)) {
			return false;
		}
		return true;
	}

}
