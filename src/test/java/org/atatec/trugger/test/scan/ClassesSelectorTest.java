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
package org.atatec.trugger.test.scan;

import org.atatec.trugger.scan.PackageScan;
import org.atatec.trugger.scan.ScanLevel;
import org.atatec.trugger.scan.impl.Scanner;
import org.atatec.trugger.scan.impl.TruggerClassesSelector;
import org.atatec.trugger.selector.ClassesSelector;
import org.atatec.trugger.test.Flag;
import org.atatec.trugger.test.SelectionTest;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.atatec.trugger.test.TruggerTest.assertResult;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ClassesSelectorTest {

  @Flag
  private class FlagAnnotated {}
  @Resource
  private class ResourceAnnotated {}

  public class PublicClass {}
  protected class ProtectedClass {}
  class DefaultClass {}
  private class PrivateClass{}

  private ClassesSelector initializeForAnnotationTest() {
    Set<Class> classes = new HashSet<Class>(){{
      add(FlagAnnotated.class);
      add(ResourceAnnotated.class);
      add(ClassesSelectorTest.class);
    }};
    return initialize(classes);
  }

  private ClassesSelector initializeForAnonymousTest() {
    Set<Class> classes = new HashSet<Class>(){{
      add(String.class);
      add(Object.class);
      add(ClassesSelectorTest.class);
      add(getClass());
    }};
    return initialize(classes);
  }

  private ClassesSelector initializeForAccessTest() {
    Set<Class> classes = new HashSet<Class>(){{
      add(PublicClass.class);
      add(ProtectedClass.class);
      add(DefaultClass.class);
      add(PrivateClass.class);
    }};
    return initialize(classes);
  }

  private ClassesSelector initialize(Set<Class> classesToReturn) {
    Scanner scanner = createMock(Scanner.class);
    try {
      expect(scanner.scanPackage(packageScan)).andReturn(classesToReturn).anyTimes();
    } catch(Exception e) {
      throw new Error(e);
    }
    replay(scanner);
    return new TruggerClassesSelector(scanner);
  }

  private PackageScan packageScan = ScanLevel.PACKAGE.createScanPackage("test.package");

  @Test
  public void testAnnotatedSelector() throws Exception {
    assertResult(new SelectionTest<ClassesSelector, Set<Class>>() {
      public ClassesSelector createSelector() {
        return initializeForAnnotationTest();
      }
      public void makeSelections(ClassesSelector selector) {
        selector.annotated();
      }
      public void assertions(Set<Class> set) {
        assertEquals(2, set.size());
        assertTrue(set.contains(FlagAnnotated.class));
        assertTrue(set.contains(ResourceAnnotated.class));
      }
    }, packageScan);
  }

  @Test
  public void testAnnotatedWithSelector() throws Exception {
    assertResult(new SelectionTest<ClassesSelector, Set<Class>>() {
      public ClassesSelector createSelector() {
        return initializeForAnnotationTest();
      }
      public void makeSelections(ClassesSelector selector) {
        selector.annotatedWith(Flag.class);
      }
      public void assertions(Set<Class> set) {
        assertEquals(1, set.size());
        assertTrue(set.contains(FlagAnnotated.class));
      }
    }, packageScan);
  }

  @Test
  public void testNotAnnotatedSelector() throws Exception {
    assertResult(new SelectionTest<ClassesSelector, Set<Class>>() {
      public ClassesSelector createSelector() {
        return initializeForAnnotationTest();
      }
      public void makeSelections(ClassesSelector selector) {
        selector.notAnnotated();
      }
      public void assertions(Set<Class> set) {
        assertEquals(1, set.size());
        assertTrue(set.contains(ClassesSelectorTest.class));
      }
    }, packageScan);
  }

  @Test
  public void testNotAnnotatedWithSelector() throws Exception {
    assertResult(new SelectionTest<ClassesSelector, Set<Class>>() {
      public ClassesSelector createSelector() {
        return initializeForAnnotationTest();
      }
      public void makeSelections(ClassesSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
      public void assertions(Set<Class> set) {
        assertEquals(2, set.size());
        assertTrue(set.contains(ClassesSelectorTest.class));
        assertTrue(set.contains(ResourceAnnotated.class));
      }
    }, packageScan);
  }

  @Test
  public void testAssignableToSelector() throws Exception {
    Set<Class> set = initialize(new HashSet<Class>(){{
      add(String.class);
      add(StringBuilder.class);
      add(StringBuffer.class);
      add(Object.class);
      add(Map.class);
      add(CharSequence.class);
    }}).assignableTo(CharSequence.class).in(packageScan);

    assertEquals(4, set.size());
    assertTrue(set.contains(String.class));
    assertTrue(set.contains(StringBuilder.class));
    assertTrue(set.contains(StringBuffer.class));
    assertTrue(set.contains(CharSequence.class));
  }

}
