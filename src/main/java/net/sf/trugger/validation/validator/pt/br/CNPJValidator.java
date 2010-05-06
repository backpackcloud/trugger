/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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

/**
 * The implementation of the {@link CNPJ} validation.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.3
 */
public class CNPJValidator implements Validator<String> {

  @Override
  public boolean isValid(@NotEmpty String value) {
    if (!value.matches("^\\d{14}$")) {
      if (!value.matches("^\\d{2}(\\.\\d{3}){2}/\\d{4}-\\d{2}$")) {
        return false;
      }
      value = value.replaceAll("\\D", "");
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

  private int calculateVerifierDigit(String digits) {
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
