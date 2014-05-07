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

import org.atatec.trugger.validation.Validator;

/**
 * Validator that checks against a defined pattern in the String representation
 * of an object.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public class PatternValidator implements Validator {

  private final String pattern;
  private final int flags;

  public PatternValidator(String pattern, int flags) {
    this.pattern = pattern;
    this.flags = flags;
  }

  public PatternValidator(Pattern constraint) {
    this(constraint.value(), constraint.flags());
  }

  @Override
  public boolean isValid(@NotNull Object value) {
    return java.util.regex.Pattern.compile(pattern, flags)
        .matcher(value.toString()).matches();
  }

}
