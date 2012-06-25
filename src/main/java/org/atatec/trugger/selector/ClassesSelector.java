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

import java.lang.annotation.Annotation;
import java.util.Set;

import org.atatec.trugger.predicate.Predicate;
import org.atatec.trugger.reflection.Access;
import org.atatec.trugger.scan.ClassScanResult;

/**
 * Interface that defines a selector for {@link Class} objects.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.3
 */
public interface ClassesSelector extends ClassSpecifier, ClassScanResult<Set<Class>> {

  ClassesSelector withAccess(Access access);

  ClassesSelector that(Predicate<? super Class> predicate);

  ClassesSelector annotatedWith(Class<? extends Annotation> type);

  ClassesSelector notAnnotatedWith(Class<? extends Annotation> type);

  ClassesSelector annotated();

  ClassesSelector notAnnotated();

  ClassesSelector anonymous();

  ClassesSelector nonAnonymous();

  ClassesSelector recursively();

  ClassesSelector assignableTo(Class type);

}
