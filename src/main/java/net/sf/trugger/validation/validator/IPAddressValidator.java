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
package net.sf.trugger.validation.validator;

import java.util.regex.Pattern;

import net.sf.trugger.validation.Validator;

/**
 * Implementation of the {@link IPAddress} validation.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
public class IPAddressValidator implements Validator<CharSequence> {

  /*
   * ^           # Assert position at the beginning of a line (at beginning of the string or after a line break character)
   * (?:               # Match the regular expression below
   *    (?:               # Match the regular expression below
   *                         # Match either the regular expression below (attempting the next alternative only if this one fails)
   *          25                # Match the characters "25" literally
   *          [0-5]             # Match a single character in the range between "0" and "5"
   *       |                 # Or match regular expression number 2 below (attempting the next alternative only if this one fails)
   *          2                 # Match the character "2" literally
   *          [0-4]             # Match a single character in the range between "0" and "4"
   *          [0-9]             # Match a single character in the range between "0" and "9"
   *       |                 # Or match regular expression number 3 below (the entire group fails if this one fails to match)
   *          [01]              # Match a single character present in the list "01"
   *             ?                 # Between zero and one times, as many times as possible, giving back as needed (greedy)
   *          [0-9]             # Match a single character in the range between "0" and "9"
   *          [0-9]             # Match a single character in the range between "0" and "9"
   *             ?                 # Between zero and one times, as many times as possible, giving back as needed (greedy)
   *    )
   *    \.                # Match the character "." literally
   * ){3}              # Exactly 3 times
   * (?:               # Match the regular expression below
   *                      # Match either the regular expression below (attempting the next alternative only if this one fails)
   *       25                # Match the characters "25" literally
   *       [0-5]             # Match a single character in the range between "0" and "5"
   *    |                 # Or match regular expression number 2 below (attempting the next alternative only if this one fails)
   *       2                 # Match the character "2" literally
   *       [0-4]             # Match a single character in the range between "0" and "4"
   *       [0-9]             # Match a single character in the range between "0" and "9"
   *    |                 # Or match regular expression number 3 below (the entire group fails if this one fails to match)
   *       [01]              # Match a single character present in the list "01"
   *          ?                 # Between zero and one times, as many times as possible, giving back as needed (greedy)
   *       [0-9]             # Match a single character in the range between "0" and "9"
   *       [0-9]             # Match a single character in the range between "0" and "9"
   *          ?                 # Between zero and one times, as many times as possible, giving back as needed (greedy)
   * )
   * $           # Assert position at the end of a line (at the end of the string or before a line break character)
   */
  private static final Pattern pattern =
      Pattern
          .compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

  public boolean isValid(@NotNull CharSequence value) {
    return pattern.matcher(value).matches();
  }

}
