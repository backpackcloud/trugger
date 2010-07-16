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
package net.sf.trugger.test.reflection;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * A suite for testing the reflection module.
 *
 * @author Marcelo Varella Barca Guimarães
 */
@RunWith(Suite.class)
@SuiteClasses( {
  ConstructorTest.class,
  GenericTypeTest.class,
  ReflectionTests.class,
  FieldReflectionTest.class,
  MethodReflectionTest.class,

  FieldSelectorTest.class,
  FieldsSelectorTest.class,
  MethodSelectorTest.class,
  MethodsSelectorTest.class,
  ConstructorSelectorTest.class,
  NoNamedFieldSelectorTest.class,
  ConstructorsSelectorTest.class,
  GetterMethodSelectorTest.class,
  SetterMethodSelectorTest.class,
  NoNamedMethodSelectorTest.class,
  FieldGetterMethodSelectorTest.class,
  FieldSetterMethodSelectorTest.class
})
public interface ReflectionTestSuite {

}
