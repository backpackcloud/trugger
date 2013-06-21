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
package org.atatec.trugger.validation.validator.pt.br;

import org.atatec.trugger.validation.Validator;
import org.atatec.trugger.validation.validator.NotEmpty;

import java.util.regex.Pattern;

/**
 * The implementation of the {@link CNPJ} validation.
 *
 * @author Marcelo Guimarães
 * @since 2.3
 */
public class CNPJValidator implements Validator<String> {

  private static final Pattern DIGITS_PATTERN = Pattern.compile("^\\d{14}$");
  private static final Pattern FORMAT_PATTERN = Pattern.compile("^\\d{2}(\\.\\d{3}){2}/\\d{4}-\\d{2}$");
  private static final Pattern NON_DIGITS_PATTERN = Pattern.compile("\\D");

  @Override
  public boolean isValid(@NotEmpty String value) {
    if (!DIGITS_PATTERN.matcher(value).matches()) {
      if (!FORMAT_PATTERN.matcher(value).matches()) {
        return false;
      }
      value = NON_DIGITS_PATTERN.matcher(value).replaceAll("");
    }
    if (value.length() != 14) {
      return false;
    }
    int d1 = calculateVerifierDigit(value.substring(0, 12));
    if (d1 != Integer.parseInt(value.substring(12, 13))) {
      return false;
    }
    int d2 = calculateVerifierDigit(value.substring(0, 12) + d1);
    return d2 == Integer.parseInt(value.substring(13, 14));
  }

  private static int calculateVerifierDigit(String digits) {
    int sum = 0;
    int length = digits.length();
    for (int i = 0 , j = length - 7 ; i < length ; i++) {
      sum += Integer.parseInt(digits.substring(i, i + 1)) * j;
      j--;
      if(j < 2) {
        j = 9;
      }
    }
    int dv = 11 - (sum % 11);
    return dv == 10 ? 0 : dv;
  }

}
