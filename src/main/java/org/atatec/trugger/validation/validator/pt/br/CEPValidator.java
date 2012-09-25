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
package org.atatec.trugger.validation.validator.pt.br;

import org.atatec.trugger.validation.Validator;
import org.atatec.trugger.validation.validator.NotEmpty;

import java.util.regex.Pattern;

/**
 * The implementation of the {@link CEP} validation.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.3
 */
public class CEPValidator implements Validator<String> {

  private static final Pattern pattern = Pattern.compile("^(\\d{5}-?\\d{3})$");

  public boolean isValid(@NotEmpty String value) {
    return pattern.matcher(value).matches();
  }

}
