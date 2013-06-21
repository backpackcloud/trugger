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
package org.atatec.trugger.selector;

import org.atatec.trugger.Result;
import org.atatec.trugger.predicate.Predicate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * Interface that defines a selector for {@link Field} objects.
 *
 * @author Marcelo Guimarães
 */
public interface FieldsSelector extends Result<Set<Field>, Object>, FieldSpecifier {

  FieldsSelector annotated();

  FieldsSelector notAnnotated();

  FieldsSelector that(Predicate<? super Field> predicate);

  FieldsSelector annotatedWith(Class<? extends Annotation> type);

  FieldsSelector notAnnotatedWith(Class<? extends Annotation> type);

  FieldsSelector nonStatic();

  FieldsSelector recursively();

  FieldsSelector ofType(Class<?> type);

  FieldsSelector assignableTo(Class<?> type);

  FieldsSelector nonFinal();

}
