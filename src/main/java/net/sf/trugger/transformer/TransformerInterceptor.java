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
package net.sf.trugger.transformer;

import static net.sf.trugger.reflection.ReflectionPredicates.methodNamed;
import net.sf.trugger.interception.ArgumentsInterceptor;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class TransformerInterceptor extends ArgumentsInterceptor {

  public TransformerInterceptor() {
    ifMethodMatches(methodNamed("transform")).useArgument(0).andCheckGenericType("From");
    ifMethodMatches(methodNamed("inverse")).useArgument(0).andCheckGenericType("To");
  }

  protected Object onInvalidArgument(Object argument) {
    return null;
  }

}
