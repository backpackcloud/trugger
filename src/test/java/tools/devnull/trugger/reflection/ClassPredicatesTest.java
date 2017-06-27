/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.devnull.trugger.reflection;

import org.junit.Test;
import tools.devnull.trugger.Selection;
import tools.devnull.trugger.reflection.impl.MemberFinder;
import tools.devnull.trugger.reflection.impl.MembersFinder;
import tools.devnull.trugger.reflection1.SomeInterface;

import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static tools.devnull.trugger.reflection.ClassPredicates.fromBasePackage;
import static tools.devnull.trugger.reflection.ClassPredicates.fromPackage;

public class ClassPredicatesTest {

  @Test
  public void testFromPackage() {
    Package p = ClassPredicates.class.getPackage();
    Predicate<Class> predicate = fromPackage(p);
    assertTrue(predicate.test(ClassPredicates.class));
    assertTrue(predicate.test(Reflector.class));
    assertFalse(predicate.test(Selection.class));
    assertFalse(predicate.test(MemberFinder.class));
    assertFalse(predicate.test(MembersFinder.class));
    assertFalse(predicate.test(SomeInterface.class));
  }

  @Test
  public void testFromBasePackage() {
    Package p = ClassPredicates.class.getPackage();
    Predicate<Class> predicate = fromBasePackage(p);
    assertTrue(predicate.test(ClassPredicates.class));
    assertTrue(predicate.test(Reflector.class));
    assertFalse(predicate.test(Selection.class));
    assertTrue(predicate.test(MemberFinder.class));
    assertTrue(predicate.test(MembersFinder.class));
    assertFalse(predicate.test(SomeInterface.class));
  }

}
