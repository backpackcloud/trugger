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

package org.atatec.trugger.validation.validator;

import org.atatec.trugger.validation.MergeElements;
import org.atatec.trugger.validation.ValidatorClass;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that the element must be valid using a validation engine and its
 * invalid elements will be included in the validation result.
 *
 * This constraint also applies to Lists and Arrays but can lead to a overhead
 * in case of many items because of the invalid elements inclusion.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
@Documented
@MergeElements
@Retention(RetentionPolicy.RUNTIME)
@ValidatorClass(ValidValidator.class)
public @interface Valid {
}
