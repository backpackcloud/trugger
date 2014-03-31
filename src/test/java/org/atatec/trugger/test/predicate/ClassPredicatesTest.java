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

package org.atatec.trugger.test.predicate;

import org.junit.Test;

import static org.atatec.trugger.test.TruggerTest.assertMatch;
import static org.atatec.trugger.reflection.ClassPredicates.*;
import static org.atatec.trugger.test.TruggerTest.assertNotMatch;

public class ClassPredicatesTest {

  @Test
  public void testSubtypePredicate() {
    assertMatch(Exception.class, subtypeOf(Throwable.class));
    assertNotMatch(Throwable.class, subtypeOf(Exception.class));
    assertNotMatch(Throwable.class, subtypeOf(Throwable.class));
  }

  @Test
  public void testTypePredicate() {
    assertNotMatch(Exception.class, type(Throwable.class));
    assertNotMatch(Throwable.class, type(Exception.class));
    assertMatch(Throwable.class, type(Throwable.class));
  }

}
