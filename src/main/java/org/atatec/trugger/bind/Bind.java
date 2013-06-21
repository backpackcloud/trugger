/*
 * Copyright 2009-2012 Marcelo Guimarães
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
package org.atatec.trugger.bind;

import org.atatec.trugger.loader.ImplementationLoader;

/**
 * A class for helping binding operations.
 *
 * @author Marcelo Guimarães
 * @since 1.2
 */
public final class Bind {

  private static final BinderFactory factory;

  private Bind() {}

  static {
    factory = ImplementationLoader.instance().get(BinderFactory.class);
  }

  /**
   * Starts a new binding.
   *
   * @return the component for this operation.
   */
  public static Binder binds() {
    return factory.createBinder();
  }

}
