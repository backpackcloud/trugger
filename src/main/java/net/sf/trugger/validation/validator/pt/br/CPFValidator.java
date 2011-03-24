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
package net.sf.trugger.validation.validator.pt.br;

import net.sf.trugger.validation.Validator;
import net.sf.trugger.validation.validator.NotEmpty;

import java.util.regex.Pattern;

/**
 * The implementation of the {@link CPF} validation.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.3
 */
public class CPFValidator implements Validator<String> {

  private static final Pattern DIGITS_PATTERN = Pattern.compile("^\\d{11}$");
  private static final Pattern FORMAT_PATTERN = Pattern.compile("^\\d{3}(\\.\\d{3}){2}-\\d{2}$");
  private static final Pattern NON_DIGITS_PATTERN = Pattern.compile("\\D");

  @Override
  public boolean isValid(@NotEmpty String value) {
    if (!DIGITS_PATTERN.matcher(value).matches()) {
      if (!FORMAT_PATTERN.matcher(value).matches()) {
        return false;
      }
      value = NON_DIGITS_PATTERN.matcher(value).replaceAll("");
    }
    if (value.length() != 11) {
      return false;
    } else if(Long.parseLong(value) % 11111111111L == 0) {
      return false;
    }
    int d1 = calculateVerifierDigit(value.substring(0, 9));
    if (d1 != Integer.parseInt(value.substring(9, 10))) {
      return false;
    }
    int d2 = calculateVerifierDigit(value.substring(0, 9) + d1);
    return d2 == Integer.parseInt(value.substring(10, 11));
  }

  private static int calculateVerifierDigit(String digits) {
    int sum = 0;
    int length = digits.length();
    for (int i = 0 , j = length + 1 ; i < length ; i++ , j--) {
      sum += Integer.parseInt(digits.substring(i, i + 1)) * j;
    }
    int dv = 11 - (sum % 11);
    return dv == 10 ? 0 : dv;
  }


}
