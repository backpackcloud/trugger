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
package org.atatec.trugger.test.element;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Marcelo Varella Barca Guimarães
 */
@RunWith(Suite.class)
@SuiteClasses( {
  HandleTest.class,

  ElementTest.class,
  NonSpecificElementTest.class,

  ElementCopyTest.class,
  ElementSelectorTest.class,
  NoNamedElementSelectorTest.class,
  ElementsSelectorTest.class,

  MapElementTest.class,
  ArrayElementTest.class,
  ResultSetElementTest.class,
  AnnotationElementTest.class,
  PropertiesElementTest.class,
  ResourceBundleElementTest.class
})
public interface ElementTestSuite {

}
