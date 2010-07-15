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
package net.sf.trugger.format.impl;

import java.lang.reflect.AnnotatedElement;

import net.sf.trugger.factory.AnnotationBasedFactory;
import net.sf.trugger.format.Formatter;
import net.sf.trugger.format.FormatterClass;
import net.sf.trugger.format.FormatterFactory;
import net.sf.trugger.format.FormatterInterceptor;
import net.sf.trugger.reflection.ReflectionPredicates;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerFormatterFactory extends AnnotationBasedFactory<FormatterClass, Formatter> implements
    FormatterFactory {

  protected Formatter defaultReturn(AnnotatedElement key) {
    return new Formatter() {

      public String format(Object value) {
        return value != null ? value.toString() : "";
      }

      public Object parse(String value) {
        return value == null || value.isEmpty() ? null : value;
      }

    };
  }

  @Override
  protected Formatter instantiate(AnnotatedElement key, Class<? extends Formatter> classToCreate) throws Throwable {
    Formatter formatter;
    //don't try to subclass final classes
    if (ReflectionPredicates.FINAL_CLASS.evaluate(classToCreate)) {
      /*
       * The proxy for interface is not useful because the binds will not work.
       * So, the validator itself is returned.
       */
      formatter = super.instantiate(key, classToCreate);
    } else {
      formatter = new FormatterInterceptor().createProxy().implementing(Formatter.class).extending(classToCreate);
    }
    return bindAnnotation(key, formatter);
  }

}
