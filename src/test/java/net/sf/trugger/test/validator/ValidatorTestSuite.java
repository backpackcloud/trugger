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
package net.sf.trugger.test.validator;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * A suite for testing the validators that don't use bindings.
 *
 * @author Marcelo Varella Barca Guimarães
 */
@RunWith(Suite.class)
@SuiteClasses( {
  AfterValidatorTest.class,
  BeforeValidatorTest.class,
  AssertFalseValidatorTest.class,
  AssertTrueValidatorTest.class,
  CPFValidatorTest.class,
  CNPJValidatorTest.class,
  CEPValidatorTest.class,
  EmailValidatorTest.class,
  FutureValidatorTest.class,
  GroupValidatorTest.class,
  IPAddressValidatorTest.class,
  LengthValidatorTest.class,
  GreaterValidatorTest.class,
  LessValidatorTest.class,
  NotEmptyValidatorTest.class,
  NotNullValidatorTest.class,
  PastValidatorTest.class,
  PatternValidatorTest.class,
  RangeValidatorTest.class,
  SizeValidatorTest.class,
  ValidValidatorTest.class
})
public class ValidatorTestSuite {

}
