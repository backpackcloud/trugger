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
package net.sf.trugger.selector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import net.sf.trugger.predicate.Predicable;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.reflection.Access;

/**
 * Base interface for selecting {@link Field fields}.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.0
 */
public interface FieldSpecifier extends MemberSelector<Field>, TypedElementSelector,
    Predicable<Field> {

  FieldSpecifier annotated();
  
  FieldSpecifier notAnnotated();
  
  FieldSpecifier annotatedWith(Class<? extends Annotation> type);

  FieldSpecifier notAnnotatedWith(Class<? extends Annotation> type);

  FieldSpecifier withAccess(Access access);

  FieldSpecifier nonStatic();

  FieldSpecifier nonFinal();

  FieldSpecifier thatMatches(Predicate<? super Field> predicate);

  FieldSpecifier recursively();

  FieldSpecifier ofType(Class<?> type);
  
  FieldSpecifier assignableTo(Class<?> type);

}
