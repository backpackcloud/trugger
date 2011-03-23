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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Pattern;

import net.sf.trugger.validation.ValidatorClass;

/**
 * Indicates that the value must be a valid email address.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@ValidatorClass(EmailValidator.class)
public @interface Email {

  /**
   * A class that enumerates a set of methods to validate an email address.
   *
   * @author Marcelo Varella Barca Guimarães
   */
  public static enum EmailValidationMethod {

    /**
     * Matches a normal email address based on the RFC 2822.
     */
    /*
     * (?:                                                                 # Match the regular expression below
     *                                                                        # Match either the regular expression below (attempting the next alternative only if this one fails)
     *       [a-z0-9!#$%&'*+/=?^_`{|}~-]                                         # Match a single character present in the list below
     *                                                                              # A character in the range between "a" and "z"
     *                                                                              # A character in the range between "0" and "9"
     *                                                                              # One of the characters "!#$%&'*+/=?^_`{|}"
     *                                                                              # The character "~"
     *                                                                              # The character "-"
     *          +                                                                   # Between one and unlimited times, as many times as possible, giving back as needed (greedy)
     *       (?:                                                                 # Match the regular expression below
     *          \.                                                                  # Match the character "." literally
     *          [a-z0-9!#$%&'*+/=?^_`{|}~-]                                         # Match a single character present in the list below
     *                                                                                 # A character in the range between "a" and "z"
     *                                                                                 # A character in the range between "0" and "9"
     *                                                                                 # One of the characters "!#$%&'*+/=?^_`{|}"
     *                                                                                 # The character "~"
     *                                                                                 # The character "-"
     *             +                                                                   # Between one and unlimited times, as many times as possible, giving back as needed (greedy)
     *       )*                                                                  # Between zero and unlimited times, as many times as possible, giving back as needed (greedy)
     *    |                                                                   # Or match regular expression number 2 below (the entire group fails if this one fails to match)
     *       "                                                                   # Match the character """ literally
     *       (?:                                                                 # Match the regular expression below
     *          |                                                                   # Match either the regular expression below (attempting the next alternative only if this one fails)
     *             [\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]                  # Match a single character present in the list below
     *                                                                                    # A character in the range between ASCII character 0x01 (1 decimal) and ASCII character 0x08 (8 decimal)
     *                                                                                    # ASCII character 0x0b (11 decimal)
     *                                                                                    # ASCII character 0x0c (12 decimal)
     *                                                                                    # A character in the range between ASCII character 0x0e (14 decimal) and ASCII character 0x1f (31 decimal)
     *                                                                                    # ASCII character 0x21 (33 decimal)
     *                                                                                    # A character in the range between ASCII character 0x23 (35 decimal) and ASCII character 0x5b (91 decimal)
     *                                                                                    # A character in the range between ASCII character 0x5d (93 decimal) and ASCII character 0x7f (127 decimal)
     *          |                                                                   # Or match regular expression number 2 below (the entire group fails if this one fails to match)
     *             \\                                                                  # Match the character "\" literally
     *             [\x01-\x09\x0b\x0c\x0e-\x7f]                                        # Match a single character present in the list below
     *                                                                                    # A character in the range between ASCII character 0x01 (1 decimal) and ASCII character 0x09 (9 decimal)
     *                                                                                    # ASCII character 0x0b (11 decimal)
     *                                                                                    # ASCII character 0x0c (12 decimal)
     *                                                                                    # A character in the range between ASCII character 0x0e (14 decimal) and ASCII character 0x7f (127 decimal)
     *       )*                                                                  # Between zero and unlimited times, as many times as possible, giving back as needed (greedy)
     *       "                                                                   # Match the character """ literally
     * )
     * @                                                                   # Match the character "@" literally
     * (?:                                                                 # Match the regular expression below
     *                                                                        # Match either the regular expression below (attempting the next alternative only if this one fails)
     *       (?:                                                                 # Match the regular expression below
     *          [a-z0-9]                                                            # Match a single character present in the list below
     *                                                                                 # A character in the range between "a" and "z"
     *                                                                                 # A character in the range between "0" and "9"
     *          (?:                                                                 # Match the regular expression below
     *             [a-z0-9-]                                                           # Match a single character present in the list below
     *                                                                                    # A character in the range between "a" and "z"
     *                                                                                    # A character in the range between "0" and "9"
     *                                                                                    # The character "-"
     *                *                                                                   # Between zero and unlimited times, as many times as possible, giving back as needed (greedy)
     *             [a-z0-9]                                                            # Match a single character present in the list below
     *                                                                                    # A character in the range between "a" and "z"
     *                                                                                    # A character in the range between "0" and "9"
     *          )?                                                                  # Between zero and one times, as many times as possible, giving back as needed (greedy)
     *          \.                                                                  # Match the character "." literally
     *       )+                                                                  # Between one and unlimited times, as many times as possible, giving back as needed (greedy)
     *       [a-z0-9]                                                            # Match a single character present in the list below
     *                                                                              # A character in the range between "a" and "z"
     *                                                                              # A character in the range between "0" and "9"
     *       (?:                                                                 # Match the regular expression below
     *          [a-z0-9-]                                                           # Match a single character present in the list below
     *                                                                                 # A character in the range between "a" and "z"
     *                                                                                 # A character in the range between "0" and "9"
     *                                                                                 # The character "-"
     *             *                                                                   # Between zero and unlimited times, as many times as possible, giving back as needed (greedy)
     *          [a-z0-9]                                                            # Match a single character present in the list below
     *                                                                                 # A character in the range between "a" and "z"
     *                                                                                 # A character in the range between "0" and "9"
     *       )?                                                                  # Between zero and one times, as many times as possible, giving back as needed (greedy)
     *    |                                                                   # Or match regular expression number 2 below (the entire group fails if this one fails to match)
     *       \[                                                                  # Match the character "[" literally
     *       (?:                                                                 # Match the regular expression below
     *          (?:                                                                 # Match the regular expression below
     *                                                                                 # Match either the regular expression below (attempting the next alternative only if this one fails)
     *                25                                                                  # Match the characters "25" literally
     *                [0-5]                                                               # Match a single character in the range between "0" and "5"
     *             |                                                                   # Or match regular expression number 2 below (attempting the next alternative only if this one fails)
     *                2                                                                   # Match the character "2" literally
     *                [0-4]                                                               # Match a single character in the range between "0" and "4"
     *                [0-9]                                                               # Match a single character in the range between "0" and "9"
     *             |                                                                   # Or match regular expression number 3 below (the entire group fails if this one fails to match)
     *                [01]                                                                # Match a single character present in the list "01"
     *                   ?                                                                   # Between zero and one times, as many times as possible, giving back as needed (greedy)
     *                [0-9]                                                               # Match a single character in the range between "0" and "9"
     *                [0-9]                                                               # Match a single character in the range between "0" and "9"
     *                   ?                                                                   # Between zero and one times, as many times as possible, giving back as needed (greedy)
     *          )
     *          \.                                                                  # Match the character "." literally
     *       ){3}                                                                # Exactly 3 times
     *       (?:                                                                 # Match the regular expression below
     *                                                                              # Match either the regular expression below (attempting the next alternative only if this one fails)
     *             25                                                                  # Match the characters "25" literally
     *             [0-5]                                                               # Match a single character in the range between "0" and "5"
     *          |                                                                   # Or match regular expression number 2 below (attempting the next alternative only if this one fails)
     *             2                                                                   # Match the character "2" literally
     *             [0-4]                                                               # Match a single character in the range between "0" and "4"
     *             [0-9]                                                               # Match a single character in the range between "0" and "9"
     *          |                                                                   # Or match regular expression number 3 below (attempting the next alternative only if this one fails)
     *             [01]                                                                # Match a single character present in the list "01"
     *                ?                                                                   # Between zero and one times, as many times as possible, giving back as needed (greedy)
     *             [0-9]                                                               # Match a single character in the range between "0" and "9"
     *             [0-9]                                                               # Match a single character in the range between "0" and "9"
     *                ?                                                                   # Between zero and one times, as many times as possible, giving back as needed (greedy)
     *          |                                                                   # Or match regular expression number 4 below (the entire group fails if this one fails to match)
     *             [a-z0-9-]                                                           # Match a single character present in the list below
     *                                                                                    # A character in the range between "a" and "z"
     *                                                                                    # A character in the range between "0" and "9"
     *                                                                                    # The character "-"
     *                *                                                                   # Between zero and unlimited times, as many times as possible, giving back as needed (greedy)
     *             [a-z0-9]                                                            # Match a single character present in the list below
     *                                                                                    # A character in the range between "a" and "z"
     *                                                                                    # A character in the range between "0" and "9"
     *             :                                                                   # Match the character ":" literally
     *             (?:                                                                 # Match the regular expression below
     *                |                                                                   # Match either the regular expression below (attempting the next alternative only if this one fails)
     *                   [\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]                      # Match a single character present in the list below
     *                                                                                          # A character in the range between ASCII character 0x01 (1 decimal) and ASCII character 0x08 (8 decimal)
     *                                                                                          # ASCII character 0x0b (11 decimal)
     *                                                                                          # ASCII character 0x0c (12 decimal)
     *                                                                                          # A character in the range between ASCII character 0x0e (14 decimal) and ASCII character 0x1f (31 decimal)
     *                                                                                          # A character in the range between ASCII character 0x21 (33 decimal) and ASCII character 0x5a (90 decimal)
     *                                                                                          # A character in the range between ASCII character 0x53 (83 decimal) and ASCII character 0x7f (127 decimal)
     *                |                                                                   # Or match regular expression number 2 below (the entire group fails if this one fails to match)
     *                   \\                                                                  # Match the character "\" literally
     *                   [\x01-\x09\x0b\x0c\x0e-\x7f]                                        # Match a single character present in the list below
     *                                                                                          # A character in the range between ASCII character 0x01 (1 decimal) and ASCII character 0x09 (9 decimal)
     *                                                                                          # ASCII character 0x0b (11 decimal)
     *                                                                                          # ASCII character 0x0c (12 decimal)
     *                                                                                          # A character in the range between ASCII character 0x0e (14 decimal) and ASCII character 0x7f (127 decimal)
     *             )+                                                                  # Between one and unlimited times, as many times as possible, giving back as needed (greedy)
     *       )
     *       \]                                                                  # Match the character "]" literally
     * )
     */
    RFC_2822("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"),
    /**
     * Matches a normal email address based on the RFC 2822, but does not check
     * the top-level domain. (Like an IP address instead of its alias.)
     */
    /*
     * [a-z0-9!#$%&'*+/=?^_`{|}~-]       # Match a single character present in the list below
     *                                      # A character in the range between "a" and "z"
     *                                      # A character in the range between "0" and "9"
     *                                      # One of the characters "!#$%&'*+/=?^_`{|}"
     *                                      # The character "~"
     *                                      # The character "-"
     *    +                                 # Between one and unlimited times, as many times as possible, giving back as needed (greedy)
     * (?:                               # Match the regular expression below
     *    \.                                # Match the character "." literally
     *    [a-z0-9!#$%&'*+/=?^_`{|}~-]       # Match a single character present in the list below
     *                                         # A character in the range between "a" and "z"
     *                                         # A character in the range between "0" and "9"
     *                                         # One of the characters "!#$%&'*+/=?^_`{|}"
     *                                         # The character "~"
     *                                         # The character "-"
     *       +                                 # Between one and unlimited times, as many times as possible, giving back as needed (greedy)
     * )*                                # Between zero and unlimited times, as many times as possible, giving back as needed (greedy)
     * @                                 # Match the character "@" literally
     * (?:                               # Match the regular expression below
     *    [a-z0-9]                          # Match a single character present in the list below
     *                                         # A character in the range between "a" and "z"
     *                                         # A character in the range between "0" and "9"
     *    (?:                               # Match the regular expression below
     *       [a-z0-9-]                         # Match a single character present in the list below
     *                                            # A character in the range between "a" and "z"
     *                                            # A character in the range between "0" and "9"
     *                                            # The character "-"
     *          *                                 # Between zero and unlimited times, as many times as possible, giving back as needed (greedy)
     *       [a-z0-9]                          # Match a single character present in the list below
     *                                            # A character in the range between "a" and "z"
     *                                            # A character in the range between "0" and "9"
     *    )?                                # Between zero and one times, as many times as possible, giving back as needed (greedy)
     *    \.                                # Match the character "." literally
     * )+                                # Between one and unlimited times, as many times as possible, giving back as needed (greedy)
     * [a-z0-9]                          # Match a single character present in the list below
     *                                      # A character in the range between "a" and "z"
     *                                      # A character in the range between "0" and "9"
     * (?:                               # Match the regular expression below
     *    [a-z0-9-]                         # Match a single character present in the list below
     *                                         # A character in the range between "a" and "z"
     *                                         # A character in the range between "0" and "9"
     *                                         # The character "-"
     *       *                                 # Between zero and unlimited times, as many times as possible, giving back as needed (greedy)
     *    [a-z0-9]                          # Match a single character present in the list below
     *                                         # A character in the range between "a" and "z"
     *                                         # A character in the range between "0" and "9"
     * )?                                # Between zero and one times, as many times as possible, giving back as needed (greedy)
     */
    RFC_2822_SIMPLIFIED("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");

    private final String regExp;

    private EmailValidationMethod(String regExp) {
      this.regExp = regExp;
    }

    /**
     * Checks if the specified email is valid.
     *
     * @param email
     *          the email address to check.
     * @return a flag indicating if the specified email address is valid.
     */
    boolean isValid(CharSequence email) {
      Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);
      return pattern.matcher(email).matches();
    }
  }

  /**
   * The method that must be used to validate the email.
   */
  EmailValidationMethod method() default EmailValidationMethod.RFC_2822_SIMPLIFIED;

  /**
   * The message for this validation or the name of the key that must be used to
   * get the message.
   */
  String message() default "validation.Email";

  /**
   * The context that this validation belongs.
   */
  String[] context() default {};

}
