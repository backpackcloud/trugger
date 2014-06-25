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
package org.atatec.trugger.test.scan;

import org.atatec.trugger.reflection.ClassPredicates;
import org.atatec.trugger.test.Flag;
import org.atatec.trugger.test.TruggerTest;
import org.junit.Test;

import java.util.List;
import java.util.function.Predicate;

import static java.lang.reflect.Modifier.PUBLIC;
import static org.atatec.trugger.reflection.ClassPredicates.declaring;
import static org.atatec.trugger.scan.ClassScan.scan;
import static org.atatec.trugger.test.TruggerTest.assertMatch;
import static org.junit.Assert.*;

/**
 * A class for testing the class finding.
 *
 * @author Marcelo Varella
 */
@Flag
public class ClassScanTest {

  private String filePackageName = "org.atatec.trugger.test.scan.classes";
  private String jarPackageName = "org.junit.runner";

  private Predicate<Class> filePackagePredicate =
      element -> element.getPackage().getName().startsWith(filePackageName);

  private Predicate<Class> jarPackagePredicate = element -> {
    String packageName = element.getPackage().getName();
    return packageName.startsWith(jarPackageName);
  };

  private int countInterfaces(String packageName) {
    List<Class> interfaces = scan().classes().deep()
        .filter(ClassPredicates.interfaceType().and(declaring(PUBLIC)))
        .in(packageName);
    return interfaces.size();
  }

  private int countClasses(String packageName) {
    List<Class> classes = scan().classes().deep()
        .filter(ClassPredicates.classType().and(declaring(PUBLIC)))
        .in(packageName);
    return classes.size();
  }

  private int countAnnotations(String packageName) {
    List<Class> annotations = scan().classes().deep()
        .filter(ClassPredicates.annotationType().and(declaring(PUBLIC)))
        .in(packageName);
    return annotations.size();
  }

  private int countEnums(String packageName) {
    List<Class> enums = scan().classes().deep()
        .filter(ClassPredicates.enumType().and(declaring(PUBLIC)))
        .in(packageName);
    return enums.size();
  }

  private int countAll(String packageName) {
    List<Class> classes =  scan().
        classes().deep().filter(declaring(PUBLIC)).in(packageName);
    TruggerTest.assertMatch(classes, declaring(PUBLIC));
    assertFalse(classes.isEmpty());
    return classes.size();
  }

  private void scanTest(String packages) {
    int classes = countClasses(packages);
    int interfaces = countInterfaces(packages);
    int annotations = countAnnotations(packages);
    int enums = countEnums(packages);
    int all = countAll(packages);
    assertTrue(all == classes + interfaces + annotations + enums);
  }

  @Test
  public void testFindInFile() {
    scanTest("org.atatec.trugger");
  }

  @Test
  public void testFindInJar() {
    scanTest("org.junit");
  }

  @Test
  public void testScanLevelInFile() {
    scanLevelTest("org.atatec.trugger");
  }

  @Test
  public void testScanLevelInJar() {
    scanLevelTest("org.junit");
  }

  private void scanLevelTest(String packageName) {
    List<Class> packageClasses = scan().classes().in(packageName);
    List<Class> subpackageClasses = scan().classes().deep().in(packageName);

    assertTrue(packageClasses.size() < subpackageClasses.size());
  }

  @Test
  public void testScanPackageInFile() {
    List<Class> list = scan().classes().in(filePackageName);
    assertEquals(4, list.size());
    assertTrue(list.contains(org.atatec.trugger.test.scan.classes.MyClass.class));
    assertTrue(list.contains(org.atatec.trugger.test.scan.classes.MyAnnotation.class));
    assertTrue(list.contains(org.atatec.trugger.test.scan.classes.MyEnum.class));
    assertTrue(list.contains(org.atatec.trugger.test.scan.classes.MyInterface.class));

    assertMatch(list, filePackagePredicate);
  }

  @Test
  public void testScanSubPackageInFile() {
    List<Class> list = scan().classes().deep().in(filePackageName);
    assertEquals(8, list.size());
    assertTrue(list.contains(org.atatec.trugger.test.scan.classes.MyClass.class));
    assertTrue(list.contains(org.atatec.trugger.test.scan.classes.pack.MyClass2.class));
    assertTrue(list.contains(org.atatec.trugger.test.scan.classes.MyAnnotation.class));
    assertTrue(list.contains(org.atatec.trugger.test.scan.classes.pack.MyAnnotation2.class));
    assertTrue(list.contains(org.atatec.trugger.test.scan.classes.MyEnum.class));
    assertTrue(list.contains(org.atatec.trugger.test.scan.classes.pack.MyEnum2.class));
    assertTrue(list.contains(org.atatec.trugger.test.scan.classes.MyInterface.class));
    assertTrue(list.contains(org.atatec.trugger.test.scan.classes.pack.MyInterface2.class));

    assertMatch(list, filePackagePredicate);
  }

  @Test
  public void testClassScanSubPackageInJar() {
    List<Class> list = scan().classes().deep()
        .filter(ClassPredicates.classType())
        .in(jarPackageName);

    assertMatch(list, jarPackagePredicate);

    assertTrue(list.contains(org.junit.runner.manipulation.NoTestsRemainException.class));
    assertTrue(list.contains(org.junit.runner.manipulation.Sorter.class));
    assertTrue(list.contains(org.junit.runner.notification.Failure.class));
    assertTrue(list.contains(org.junit.runner.notification.RunListener.class));
    assertTrue(list.contains(org.junit.runner.notification.RunNotifier.class));
    assertTrue(list.contains(org.junit.runner.notification.StoppedByUserException.class));
    assertTrue(list.contains(org.junit.runner.Computer.class));
    assertTrue(list.contains(org.junit.runner.Description.class));
    assertTrue(list.contains(org.junit.runner.JUnitCore.class));
    assertTrue(list.contains(org.junit.runner.Request.class));
    assertTrue(list.contains(org.junit.runner.Result.class));
    assertTrue(list.contains(org.junit.runner.Runner.class));
  }

  @Test
  public void testScanSubPackageInJar() {
    List<Class> list = scan().classes().deep().in(jarPackageName);

    assertMatch(list, jarPackagePredicate);

    assertTrue(list.contains(org.junit.runner.manipulation.NoTestsRemainException.class));
    assertTrue(list.contains(org.junit.runner.manipulation.Sorter.class));
    assertTrue(list.contains(org.junit.runner.notification.Failure.class));
    assertTrue(list.contains(org.junit.runner.notification.RunListener.class));
    assertTrue(list.contains(org.junit.runner.notification.RunNotifier.class));
    assertTrue(list.contains(org.junit.runner.notification.StoppedByUserException.class));
    assertTrue(list.contains(org.junit.runner.Computer.class));
    assertTrue(list.contains(org.junit.runner.Description.class));
    assertTrue(list.contains(org.junit.runner.JUnitCore.class));
    assertTrue(list.contains(org.junit.runner.Request.class));
    assertTrue(list.contains(org.junit.runner.Result.class));
    assertTrue(list.contains(org.junit.runner.Runner.class));

    assertTrue(list.contains(org.junit.runner.RunWith.class));

    assertTrue(list.contains(org.junit.runner.manipulation.Filterable.class));
    assertTrue(list.contains(org.junit.runner.manipulation.Sortable.class));
    assertTrue(list.contains(org.junit.runner.Describable.class));
  }

}
