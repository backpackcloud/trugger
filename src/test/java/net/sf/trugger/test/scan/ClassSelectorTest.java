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

import net.sf.trugger.reflection.Access;
import net.sf.trugger.scan.PackageScan;
import net.sf.trugger.scan.ScanLevel;
import net.sf.trugger.scan.impl.Scanner;
import net.sf.trugger.scan.impl.TruggerClassSelector;
import net.sf.trugger.selector.ClassSelector;
import net.sf.trugger.test.Flag;
import net.sf.trugger.test.SelectionTest;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static net.sf.trugger.test.TruggerTest.assertResult;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

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

  private ClassSelector initializeForAnnotationTest() {
    Set<Class> classes = new HashSet<Class>(){{
      add(FlagAnnotated.class);
      add(ClassSelectorTest.class);
    }};
    return initialize(classes);
  }

  private ClassSelector initializeForAnonymousTest() {
    Set<Class> classes = new HashSet<Class>(){{
      add(String.class);
      add(getClass());
    }};
    return initialize(classes);
  }

  private ClassSelector initializeForAccessTest() {
    Set<Class> classes = new HashSet<Class>(){{
      add(PublicClass.class);
      add(ProtectedClass.class);
      add(DefaultClass.class);
      add(PrivateClass.class);
    }};
    return initialize(classes);
  }

  private ClassSelector initialize(Set<Class> classesToReturn) {
    Scanner scanner = createMock(Scanner.class);
    try {
      expect(scanner.scanPackage(packageScan)).andReturn(classesToReturn).anyTimes();
    } catch(Exception e) {
      throw new Error(e);
    }
    replay(scanner);
    return new TruggerClassSelector(scanner);
  }

  private PackageScan packageScan = ScanLevel.PACKAGE.createScanPackage("test.package");

  @Test
  public void testAnnotatedSelector() throws Exception {
    assertResult(new SelectionTest<ClassSelector, Class>() {
      public ClassSelector createSelector() {
        return initializeForAnnotationTest();
      }
      public void makeSelections(ClassSelector selector) {
        selector.annotated();
      }
      public void assertions(Class c) {
        assertEquals(FlagAnnotated.class, c);
      }
    }, packageScan);
  }

  @Test
  public void testAnnotatedWithSelector() throws Exception {
    assertResult(new SelectionTest<ClassSelector, Class>() {
      public ClassSelector createSelector() {
        return initializeForAnnotationTest();
      }
      public void makeSelections(ClassSelector selector) {
        selector.annotatedWith(Flag.class);
      }
      public void assertions(Class c) {
        assertEquals(FlagAnnotated.class, c);
      }
    }, packageScan);
  }

  @Test
  public void testNotAnnotatedSelector() throws Exception {
    assertResult(new SelectionTest<ClassSelector, Class>() {
      public ClassSelector createSelector() {
        return initializeForAnnotationTest();
      }
      public void makeSelections(ClassSelector selector) {
        selector.notAnnotated();
      }
      public void assertions(Class c) {
        assertEquals(ClassSelectorTest.class, c);
      }
    }, packageScan);
  }

  @Test
  public void testNotAnnotatedWithSelector() throws Exception {
    assertResult(new SelectionTest<ClassSelector, Class>() {
      public ClassSelector createSelector() {
        return initializeForAnnotationTest();
      }
      public void makeSelections(ClassSelector selector) {
        selector.notAnnotatedWith(Flag.class);
      }
      public void assertions(Class c) {
        assertEquals(ClassSelectorTest.class, c);
      }
    }, packageScan);
  }

  @Test
  public void testAnonymousSelector() throws Exception {
    assertResult(new SelectionTest<ClassSelector, Class>() {
      public ClassSelector createSelector() {
        return initializeForAnonymousTest();
      }
      public void makeSelections(ClassSelector selector) {
        selector.anonymous();
      }
      public void assertions(Class c) {
        assertTrue(c.isAnonymousClass());
      }
    }, packageScan);
  }

  @Test
  public void testNonAnonymousSelector() throws Exception {
    assertResult(new SelectionTest<ClassSelector, Class>() {
      public ClassSelector createSelector() {
        return initializeForAnonymousTest();
      }
      public void makeSelections(ClassSelector selector) {
        selector.nonAnonymous();
      }
      public void assertions(Class c) {
        assertFalse(c.isAnonymousClass());
      }
    }, packageScan);
  }

  @Test
  public void testAccessSelector() throws Exception {
    Class c = initializeForAccessTest().withAccess(Access.PUBLIC).in(packageScan);
    assertEquals(PublicClass.class, c);

    c = initializeForAccessTest().withAccess(Access.PROTECTED).in(packageScan);
    assertEquals(ProtectedClass.class, c);

    c = initializeForAccessTest().withAccess(Access.DEFAULT).in(packageScan);
    assertEquals(DefaultClass.class, c);

    c = initializeForAccessTest().withAccess(Access.PRIVATE).in(packageScan);
    assertEquals(PrivateClass.class, c);
  }

  @Test
  public void testAssignableToSelector() throws Exception {
    Class c = initialize(new HashSet<Class>(){{
      add(String.class);
      add(Object.class);
      add(Map.class);
    }}).assignableTo(CharSequence.class).in(packageScan);

    assertEquals(String.class, c);
  }

}
