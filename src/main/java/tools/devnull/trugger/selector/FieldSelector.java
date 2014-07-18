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
package tools.devnull.trugger.selector;

import tools.devnull.trugger.Result;

import java.lang.reflect.Field;
import java.util.function.Predicate;

/**
 * Interface that defines a selector for a single {@link Field} object assuming
 * that the name was specified before.
 *
 * @author Marcelo Guimarães
 */
public interface FieldSelector extends PredicateSelector<Field>,
    DeepSelector, Result<Field, Object> {

  FieldSelector filter(Predicate<? super Field> predicate);

  FieldSelector deep();

}
