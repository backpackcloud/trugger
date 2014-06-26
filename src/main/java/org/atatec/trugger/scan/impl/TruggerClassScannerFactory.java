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
package org.atatec.trugger.scan.impl;

import org.atatec.trugger.util.registry.MapRegistry;
import org.atatec.trugger.util.registry.Registry;
import org.atatec.trugger.scan.ClassScanner;
import org.atatec.trugger.scan.ClassScannerFactory;
import org.atatec.trugger.scan.NoResourceFinderException;
import org.atatec.trugger.scan.ResourceFinder;

/**
 * The default class finder factory.
 *
 * @author Marcelo Guimarães
 */
public class TruggerClassScannerFactory implements ClassScannerFactory {

  private final Registry<String, ResourceFinder> registry = new MapRegistry<String, ResourceFinder>();

  public TruggerClassScannerFactory() {
    register(
      new FileResourceFinder(),
      new JarResourceFinder(),
      new VfsFileResourceFinder(),
      new VfsZipResourceFinder(),
      new VfsZipResourceFinder(),
      new VfsResourceFinder()
    );
  }

  @Override
  public void register(ResourceFinder... finders) {
    for (ResourceFinder finder : finders) {
      registry.register(finder).to(finder.protocol());
    }
  }

  public ResourceFinder finderFor(String protocol) {
    ResourceFinder finder = registry.registryFor(protocol.toLowerCase());
    if (finder == null) {
      throw new NoResourceFinderException("No finder registered for protocol \"" + protocol + '\"');
    }
    return finder;
  }

  /** Returns a new instance of {@link TruggerClassScanner}. */
  public ClassScanner createClassScanner() {
    return new TruggerClassScanner(this);
  }

}
