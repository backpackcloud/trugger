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

package org.atatec.trugger.interception.impl;

import org.atatec.trugger.interception.Interceptor;
import org.atatec.trugger.interception.InterceptorFactory;

public class TruggerInterceptorFactory implements InterceptorFactory {

  public Interceptor createInterceptor(Class interfaceClass) {
    return new TruggerInterceptor(new Class[]{interfaceClass});
  }

  public Interceptor createInterceptor(Class[] interfaces) {
    return new TruggerInterceptor(interfaces);
  }

  public Interceptor createInterceptor(Object target) {
    return new TruggerInterceptor(target);
  }

}
