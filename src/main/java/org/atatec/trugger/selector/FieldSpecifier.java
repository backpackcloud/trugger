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
package org.atatec.trugger.selector;

import org.atatec.trugger.predicate.Predicate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Base interface for selecting {@link Field fields}.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.0
 */
public interface FieldSpecifier extends MemberSelector<Field>, TypedElementSelector {

  FieldSpecifier annotated();

  FieldSpecifier notAnnotated();

  FieldSpecifier annotatedWith(Class<? extends Annotation> type);

  FieldSpecifier notAnnotatedWith(Class<? extends Annotation> type);

  FieldSpecifier nonStatic();

  FieldSpecifier nonFinal();

  FieldSpecifier that(Predicate<? super Field> predicate);

  FieldSpecifier recursively();

  FieldSpecifier ofType(Class<?> type);

  FieldSpecifier assignableTo(Class<?> type);

}
