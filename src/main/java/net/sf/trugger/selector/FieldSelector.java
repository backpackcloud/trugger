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
package net.sf.trugger.selector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import net.sf.trugger.Result;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.reflection.Access;

/**
 * Interface that defines a selector for a single {@link Field} object assuming
 * that the name was specified before.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public interface FieldSelector extends Result<Field, Object>, FieldSpecifier {
  
  FieldSelector annotated();
  
  FieldSelector notAnnotated();
  
  FieldSelector annotatedWith(Class<? extends Annotation> type);
  
  FieldSelector notAnnotatedWith(Class<? extends Annotation> type);
  
  FieldSelector withAccess(Access access);
  
  FieldSelector nonStatic();
  
  FieldSelector nonFinal();
  
  FieldSelector thatMatches(Predicate<? super Field> predicate);
  
  /**
   * Note: this selection does not affect the predicate returned by
   * {@link #toPredicate()}.
   */
  FieldSelector recursively();
  
  FieldSelector ofType(Class<?> type);
  
  FieldSelector assignableTo(Class<?> type);
  
}
