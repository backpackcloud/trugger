/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import net.sf.trugger.scan.ClassScannerFactory;
import net.sf.trugger.scan.ResourceFinder;
import net.sf.trugger.scan.ScanLevel;

import org.eclipse.osgi.framework.internal.core.BundleURLConnection;

/**
 * A resource finder for use in Eclipse plug-ins or RCP Applications.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class BundleResourceFinder implements ResourceFinder {

  /**
   * The protocol that this finder should be registered.
   */
  public static final String PROTOCOL = "bundleresource";
  /**
   * The finder used for find the resources.
   */
  private ClassScannerFactory factory;

  public BundleResourceFinder(ClassScannerFactory factory) {
    this.factory = factory;
  }

  public Set<String> find(URL resource, String packageName, ScanLevel scanLevel) throws IOException {
    BundleURLConnection conn = (BundleURLConnection) resource.openConnection();
    URL url = conn.getLocalURL(); // Converts the URL to a common local URL protocol
    String protocol = url.getProtocol();
    return factory.finderFor(protocol).find(url, packageName, scanLevel);
  }

}
