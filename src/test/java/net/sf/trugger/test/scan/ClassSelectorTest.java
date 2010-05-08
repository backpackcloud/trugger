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
package net.sf.trugger.test.scan;

import static net.sf.trugger.test.TruggerTest.assertResult;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.trugger.reflection.Access;
import net.sf.trugger.scan.PackageScan;
import net.sf.trugger.scan.ScanLevel;
import net.sf.trugger.scan.impl.Scanner;
import net.sf.trugger.scan.impl.TruggerClassesSelector;
import net.sf.trugger.selector.ClassesSelector;
import net.sf.trugger.test.Flag;
import net.sf.trugger.test.SelectionTest;

import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ClassSelectorTest {

  @Flag
  private class FlagAnnotated {}
  @Resource
  private class ResourceAnnotated {}

  public class PublicClass {}
  protected class ProtectedClass {}
  class DefaultClass {}
  private class PrivateClass{}

  private ClassesSelector initializeForAnnotationTest() {
    Set<Class<?>> classes = new HashSet<Class<?>>(){{
      add(FlagAnnotated.class);
      add(ResourceAnnotated.class);
      add(ClassSelectorTest.class);
    }};
    return initialize(classes);
  }

  private ClassesSelector initializeForAnonymousTest() {
    Set<Class<?>> classes = new HashSet<Class<?>>(){{
      add(String.class);
      add(Object.class);
      add(ClassSelectorTest.class);
      add(getClass());
    }};
    return initialize(classes);
  }

  private ClassesSelector initializeForAccessTest() {
    Set<Class<?>> classes = new HashSet<Class<?>>(){{
      add(PublicClass.class);
      add(ProtectedClass.class);
      add(DefaultClass.class);
      add(PrivateClass.class);
    }};
    return initialize(classes);
  }

  private ClassesSelector initialize(Set<Class<?>> classesToReturn) {
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
    assertResult(new SelectionTest<ClassesSelector, Set<Class<?>>>() {
      public ClassesSelector createSelector() {
        return initializeForAnnotationTest();
      }
      public void makeSelections(ClassesSelector selector) {
        selector.annotated();
      }
      public void assertions(Set<Class<?>> set) {
        assertEquals(2, set.size());
        assertTrue(set.contains(FlagAnnotated.class));
        assertTrue(set.contains(ResourceAnnotated.class));
      }
    }, packageScan);
  }

  @Test
  public void testAnnotatedWithSelector() throws Exception {
    assertResult(new SelectionTest<ClassesSelector, Set<Class<?>>>() {
      public ClassesSelector createSelector() {
        return initializeForAnnotationTest();
      }
      public void makeSelections(ClassesSelector selector) {
        selector.annotatedWith(Flag.class);
      }
      public void assertions(Set<Class<?>> set) {
        assertEquals(1, set.size());
        assertTrue(set.contains(FlagAnnotated.class));
      }
    }, packageScan);
  }

  @Test
  public void testNotAnnotatedSelector() throws Exception {
    assertResult(new SelectionTest<ClassesSelector, Set<Class<?>>>() {
      public ClassesSelector createSelector() {
        return initializeForAnnotationTest();
      }
      public void makeSelections(ClassesSelector selector) {
        selector.notAnnotated();
      }
      public void assertions(Set<Class<?>> set) {
        assertEquals(1, set.size());
        assertTrue(set.contains(ClassSelectorTest.class));
      }
    }, packageScan);
  }

  @Test
  public void testNotAnnotatedWithSelector() throws Exception {
    assertResult(new SelectionTest<ClassesSelector, Set<Class<?>>>() {
      public ClassesSelector createSelector() {
        return initializeForAnnotationTest();
      }
      public void makeSelections(ClassesSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
      public void assertions(Set<Class<?>> set) {
        assertEquals(2, set.size());
        assertTrue(set.contains(ClassSelectorTest.class));
        assertTrue(set.contains(ResourceAnnotated.class));
      }
    }, packageScan);
  }

  @Test
  public void testAnonymousSelector() throws Exception {
    assertResult(new SelectionTest<ClassesSelector, Set<Class<?>>>() {
      public ClassesSelector createSelector() {
        return initializeForAnonymousTest();
      }
      public void makeSelections(ClassesSelector selector) {
        selector.anonymous();
      }
      public void assertions(Set<Class<?>> set) {
        assertEquals(1, set.size());
        assertFalse(set.contains(String.class));
        assertFalse(set.contains(Object.class));
        assertFalse(set.contains(ClassSelectorTest.class));
      }
    }, packageScan);
  }

  @Test
  public void testNonAnonymousSelector() throws Exception {
    assertResult(new SelectionTest<ClassesSelector, Set<Class<?>>>() {
      public ClassesSelector createSelector() {
        return initializeForAnonymousTest();
      }
      public void makeSelections(ClassesSelector selector) {
        selector.nonAnonymous();
      }
      public void assertions(Set<Class<?>> set) {
        assertEquals(3, set.size());
        assertTrue(set.contains(String.class));
        assertTrue(set.contains(Object.class));
        assertTrue(set.contains(ClassSelectorTest.class));
      }
    }, packageScan);
  }

  @Test
  public void testAccessSelector() throws Exception {
    Set<Class<?>> set = initializeForAccessTest().withAccess(Access.PUBLIC).in(packageScan);
    assertEquals(1, set.size());
    assertTrue(set.contains(PublicClass.class));

    set = initializeForAccessTest().withAccess(Access.PROTECTED).in(packageScan);
    assertEquals(1, set.size());
    assertTrue(set.contains(ProtectedClass.class));

    set = initializeForAccessTest().withAccess(Access.DEFAULT).in(packageScan);
    assertEquals(1, set.size());
    assertTrue(set.contains(DefaultClass.class));

    set = initializeForAccessTest().withAccess(Access.PRIVATE).in(packageScan);
    assertEquals(1, set.size());
    assertTrue(set.contains(PrivateClass.class));

    set = initializeForAccessTest().withAccess(Access.LIKE_PROTECTED).in(packageScan);
    assertEquals(2, set.size());
    assertTrue(set.contains(PublicClass.class));
    assertTrue(set.contains(ProtectedClass.class));

    set = initializeForAccessTest().withAccess(Access.LIKE_DEFAULT).in(packageScan);
    assertEquals(3, set.size());
    assertTrue(set.contains(PublicClass.class));
    assertTrue(set.contains(ProtectedClass.class));
    assertTrue(set.contains(DefaultClass.class));
  }

  @Test
  public void testAssignableToSelector() throws Exception {
    Set<Class<?>> set = initialize(new HashSet<Class<?>>(){{
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
