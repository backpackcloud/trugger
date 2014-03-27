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
import org.atatec.trugger.scan.PackageScan;
import org.atatec.trugger.scan.ScanLevel;
import org.atatec.trugger.test.Flag;
import org.atatec.trugger.test.TruggerTest;
import org.junit.Test;

import java.util.List;
import java.util.function.Predicate;

import static java.lang.reflect.Modifier.PUBLIC;
import static org.atatec.trugger.reflection.ClassPredicates.declare;
import static org.atatec.trugger.scan.ClassScan.find;
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
  private String[] jarPackageNames = new String[]{"org.junit.runner", "org.easymock"};

  private Predicate<Class> filePackagePredicate =
      element -> element.getPackage().getName().startsWith(filePackageName);

  private Predicate<Class> jarPackagePredicate = element -> {
    String packageName = element.getPackage().getName();
    return packageName.startsWith(jarPackageNames[0]) || packageName.startsWith(jarPackageNames[1]);
  };

  private int interfaceTest(String... packages) {
    List<Class> interfaces = find()
        .filter(ClassPredicates.INTERFACE.and(declare(PUBLIC)))
        .in(ScanLevel.SUBPACKAGES.createScanPackages(packages));
    return interfaces.size();
  }

  private int classesTest(String... packages) {
    List<Class> classes = find()
        .filter(ClassPredicates.CLASS.and(declare(PUBLIC)))
        .in(ScanLevel.SUBPACKAGES.createScanPackages(packages));
    return classes.size();
  }

  private int annotationsTest(String... packages) {
    List<Class> annotations = find()
        .filter(ClassPredicates.ANNOTATION.and(declare(PUBLIC)))
        .in(ScanLevel.SUBPACKAGES.createScanPackages(packages));
    return annotations.size();
  }

  private int enumsTest(String... packages) {
    List<Class> enums = find()
        .filter(ClassPredicates.ENUM.and(declare(PUBLIC)))
        .in(ScanLevel.SUBPACKAGES.createScanPackages(packages));
    return enums.size();
  }

  private int allTest(String... packages) {
    List<Class> classes =
        find().filter(declare(PUBLIC)).in(ScanLevel.SUBPACKAGES.createScanPackages(packages));
    TruggerTest.assertMatch(classes, declare(PUBLIC));
    assertFalse(classes.isEmpty());
    return classes.size();
  }

  private void scanTest(String... packages) {
    int classes = classesTest(packages);
    int interfaces = interfaceTest(packages);
    int annotations = annotationsTest(packages);
    int enums = enumsTest(packages);
    int all = allTest(packages);
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
    List<Class> packageClasses = find().in(packageName);
    List<Class> subpackageClasses = find().recursively().in(packageName);

    assertTrue(packageClasses.size() < subpackageClasses.size());
  }

  @Test
  public void testEqualsAndHash() {
    PackageScan scan1 = new PackageScan("org.atatec.trugger", ScanLevel.PACKAGE);
    PackageScan scan2 = new PackageScan("org.atatec.trugger", ScanLevel.SUBPACKAGES);

    assertFalse(scan1.equals(scan2));
    assertFalse(scan1.hashCode() == scan2.hashCode());

    scan1 = new PackageScan("org.atatec.trugger", ScanLevel.PACKAGE);
    scan2 = new PackageScan("org.atatec", ScanLevel.PACKAGE);

    assertFalse(scan1.equals(scan2));
    assertFalse(scan1.hashCode() == scan2.hashCode());

    scan2 = new PackageScan("org.atatec.trugger", ScanLevel.PACKAGE);

    assertTrue(scan1.equals(scan2));
    assertTrue(scan1.hashCode() == scan2.hashCode());
  }

  @Test
  public void testScanPackageInFile() {
    List<Class> set = find().in(filePackageName);
    assertEquals(4, set.size());
    assertTrue(set.contains(org.atatec.trugger.test.scan.classes.MyClass.class));
    assertTrue(set.contains(org.atatec.trugger.test.scan.classes.MyAnnotation.class));
    assertTrue(set.contains(org.atatec.trugger.test.scan.classes.MyEnum.class));
    assertTrue(set.contains(org.atatec.trugger.test.scan.classes.MyInterface.class));

    assertMatch(set, filePackagePredicate);
  }

  @Test
  public void testScanSubPackageInFile() {
    List<Class> set = find().recursively().in(filePackageName);
    assertEquals(8, set.size());
    assertTrue(set.contains(org.atatec.trugger.test.scan.classes.MyClass.class));
    assertTrue(set.contains(org.atatec.trugger.test.scan.classes.pack.MyClass2.class));
    assertTrue(set.contains(org.atatec.trugger.test.scan.classes.MyAnnotation.class));
    assertTrue(set.contains(org.atatec.trugger.test.scan.classes.pack.MyAnnotation2.class));
    assertTrue(set.contains(org.atatec.trugger.test.scan.classes.MyEnum.class));
    assertTrue(set.contains(org.atatec.trugger.test.scan.classes.pack.MyEnum2.class));
    assertTrue(set.contains(org.atatec.trugger.test.scan.classes.MyInterface.class));
    assertTrue(set.contains(org.atatec.trugger.test.scan.classes.pack.MyInterface2.class));

    assertMatch(set, filePackagePredicate);
  }

  @Test
  public void testClassScanSubPackageInJar() {
    List<Class> set = find()
        .filter(ClassPredicates.CLASS)
        .recursively()
        .in(jarPackageNames);

    assertMatch(set, jarPackagePredicate);

    assertTrue(set.contains(org.junit.runner.manipulation.NoTestsRemainException.class));
    assertTrue(set.contains(org.junit.runner.manipulation.Sorter.class));
    assertTrue(set.contains(org.junit.runner.notification.Failure.class));
    assertTrue(set.contains(org.junit.runner.notification.RunListener.class));
    assertTrue(set.contains(org.junit.runner.notification.RunNotifier.class));
    assertTrue(set.contains(org.junit.runner.notification.StoppedByUserException.class));
    assertTrue(set.contains(org.junit.runner.Computer.class));
    assertTrue(set.contains(org.junit.runner.Description.class));
    assertTrue(set.contains(org.junit.runner.JUnitCore.class));
    assertTrue(set.contains(org.junit.runner.Request.class));
    assertTrue(set.contains(org.junit.runner.Result.class));
    assertTrue(set.contains(org.junit.runner.Runner.class));

    assertTrue(set.contains(org.easymock.internal.matchers.And.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Any.class));
    assertTrue(set.contains(org.easymock.internal.matchers.ArrayEquals.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Captures.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Compare.class));
    assertTrue(set.contains(org.easymock.internal.matchers.CompareEqual.class));
    assertTrue(set.contains(org.easymock.internal.matchers.CompareTo.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Contains.class));
    assertTrue(set.contains(org.easymock.internal.matchers.EndsWith.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Equals.class));
    assertTrue(set.contains(org.easymock.internal.matchers.EqualsWithDelta.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Find.class));
    assertTrue(set.contains(org.easymock.internal.matchers.GreaterOrEqual.class));
    assertTrue(set.contains(org.easymock.internal.matchers.GreaterThan.class));
    assertTrue(set.contains(org.easymock.internal.matchers.InstanceOf.class));
    assertTrue(set.contains(org.easymock.internal.matchers.LessOrEqual.class));
    assertTrue(set.contains(org.easymock.internal.matchers.LessThan.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Matches.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Not.class));
    assertTrue(set.contains(org.easymock.internal.matchers.NotNull.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Null.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Or.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Same.class));
    assertTrue(set.contains(org.easymock.internal.matchers.StartsWith.class));
    assertTrue(set.contains(org.easymock.internal.AlwaysMatcher.class));
    assertTrue(set.contains(org.easymock.internal.ArgumentToString.class));
    assertTrue(set.contains(org.easymock.internal.ArrayMatcher.class));
    assertTrue(set.contains(org.easymock.internal.AssertionErrorWrapper.class));
    assertTrue(set.contains(org.easymock.internal.EasyMockProperties.class));
    assertTrue(set.contains(org.easymock.internal.EqualsMatcher.class));
    assertTrue(set.contains(org.easymock.internal.ErrorMessage.class));
    assertTrue(set.contains(org.easymock.internal.ExpectedInvocation.class));
    assertTrue(set.contains(org.easymock.internal.ExpectedInvocationAndResult.class));
    assertTrue(set.contains(org.easymock.internal.ExpectedInvocationAndResults.class));
    assertTrue(set.contains(org.easymock.internal.Invocation.class));
    assertTrue(set.contains(org.easymock.internal.JavaProxyFactory.class));
    assertTrue(set.contains(org.easymock.internal.LastControl.class));
    assertTrue(set.contains(org.easymock.internal.LegacyMatcherProvider.class));
    assertTrue(set.contains(org.easymock.internal.MethodSerializationWrapper.class));
    assertTrue(set.contains(org.easymock.internal.MockInvocationHandler.class));
    assertTrue(set.contains(org.easymock.internal.MocksBehavior.class));
    assertTrue(set.contains(org.easymock.internal.MocksControl.class));
    assertTrue(set.contains(org.easymock.internal.ObjectMethodsFilter.class));
    assertTrue(set.contains(org.easymock.internal.Range.class));
    assertTrue(set.contains(org.easymock.internal.RecordState.class));
    assertTrue(set.contains(org.easymock.internal.ReplayState.class));
    assertTrue(set.contains(org.easymock.internal.Result.class));
    assertTrue(set.contains(org.easymock.internal.Results.class));
    assertTrue(set.contains(org.easymock.internal.RuntimeExceptionWrapper.class));
    assertTrue(set.contains(org.easymock.internal.ThrowableWrapper.class));
    assertTrue(set.contains(org.easymock.internal.UnorderedBehavior.class));
    assertTrue(set.contains(org.easymock.AbstractMatcher.class));
    assertTrue(set.contains(org.easymock.Capture.class));
    assertTrue(set.contains(org.easymock.EasyMock.class));
    assertTrue(set.contains(org.easymock.EasyMockSupport.class));
    assertTrue(set.contains(org.easymock.MockControl.class));
  }

  @Test
  public void testScanSubPackageInJar() {
    List<Class> set = find().recursively().in(jarPackageNames);

    assertMatch(set, jarPackagePredicate);

    assertTrue(set.contains(org.junit.runner.manipulation.NoTestsRemainException.class));
    assertTrue(set.contains(org.junit.runner.manipulation.Sorter.class));
    assertTrue(set.contains(org.junit.runner.notification.Failure.class));
    assertTrue(set.contains(org.junit.runner.notification.RunListener.class));
    assertTrue(set.contains(org.junit.runner.notification.RunNotifier.class));
    assertTrue(set.contains(org.junit.runner.notification.StoppedByUserException.class));
    assertTrue(set.contains(org.junit.runner.Computer.class));
    assertTrue(set.contains(org.junit.runner.Description.class));
    assertTrue(set.contains(org.junit.runner.JUnitCore.class));
    assertTrue(set.contains(org.junit.runner.Request.class));
    assertTrue(set.contains(org.junit.runner.Result.class));
    assertTrue(set.contains(org.junit.runner.Runner.class));

    assertTrue(set.contains(org.easymock.internal.matchers.And.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Any.class));
    assertTrue(set.contains(org.easymock.internal.matchers.ArrayEquals.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Captures.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Compare.class));
    assertTrue(set.contains(org.easymock.internal.matchers.CompareEqual.class));
    assertTrue(set.contains(org.easymock.internal.matchers.CompareTo.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Contains.class));
    assertTrue(set.contains(org.easymock.internal.matchers.EndsWith.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Equals.class));
    assertTrue(set.contains(org.easymock.internal.matchers.EqualsWithDelta.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Find.class));
    assertTrue(set.contains(org.easymock.internal.matchers.GreaterOrEqual.class));
    assertTrue(set.contains(org.easymock.internal.matchers.GreaterThan.class));
    assertTrue(set.contains(org.easymock.internal.matchers.InstanceOf.class));
    assertTrue(set.contains(org.easymock.internal.matchers.LessOrEqual.class));
    assertTrue(set.contains(org.easymock.internal.matchers.LessThan.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Matches.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Not.class));
    assertTrue(set.contains(org.easymock.internal.matchers.NotNull.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Null.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Or.class));
    assertTrue(set.contains(org.easymock.internal.matchers.Same.class));
    assertTrue(set.contains(org.easymock.internal.matchers.StartsWith.class));
    assertTrue(set.contains(org.easymock.internal.AlwaysMatcher.class));
    assertTrue(set.contains(org.easymock.internal.ArgumentToString.class));
    assertTrue(set.contains(org.easymock.internal.ArrayMatcher.class));
    assertTrue(set.contains(org.easymock.internal.AssertionErrorWrapper.class));
    assertTrue(set.contains(org.easymock.internal.EasyMockProperties.class));
    assertTrue(set.contains(org.easymock.internal.EqualsMatcher.class));
    assertTrue(set.contains(org.easymock.internal.ErrorMessage.class));
    assertTrue(set.contains(org.easymock.internal.ExpectedInvocation.class));
    assertTrue(set.contains(org.easymock.internal.ExpectedInvocationAndResult.class));
    assertTrue(set.contains(org.easymock.internal.ExpectedInvocationAndResults.class));
    assertTrue(set.contains(org.easymock.internal.Invocation.class));
    assertTrue(set.contains(org.easymock.internal.JavaProxyFactory.class));
    assertTrue(set.contains(org.easymock.internal.LastControl.class));
    assertTrue(set.contains(org.easymock.internal.LegacyMatcherProvider.class));
    assertTrue(set.contains(org.easymock.internal.MethodSerializationWrapper.class));
    assertTrue(set.contains(org.easymock.internal.MockInvocationHandler.class));
    assertTrue(set.contains(org.easymock.internal.MocksBehavior.class));
    assertTrue(set.contains(org.easymock.internal.MocksControl.class));
    assertTrue(set.contains(org.easymock.internal.ObjectMethodsFilter.class));
    assertTrue(set.contains(org.easymock.internal.Range.class));
    assertTrue(set.contains(org.easymock.internal.RecordState.class));
    assertTrue(set.contains(org.easymock.internal.ReplayState.class));
    assertTrue(set.contains(org.easymock.internal.Result.class));
    assertTrue(set.contains(org.easymock.internal.Results.class));
    assertTrue(set.contains(org.easymock.internal.RuntimeExceptionWrapper.class));
    assertTrue(set.contains(org.easymock.internal.ThrowableWrapper.class));
    assertTrue(set.contains(org.easymock.internal.UnorderedBehavior.class));
    assertTrue(set.contains(org.easymock.AbstractMatcher.class));
    assertTrue(set.contains(org.easymock.Capture.class));
    assertTrue(set.contains(org.easymock.EasyMock.class));
    assertTrue(set.contains(org.easymock.EasyMockSupport.class));
    assertTrue(set.contains(org.easymock.MockControl.class));

    assertTrue(set.contains(org.junit.runner.RunWith.class));

    assertTrue(set.contains(org.junit.runner.manipulation.Filterable.class));
    assertTrue(set.contains(org.junit.runner.manipulation.Sortable.class));
    assertTrue(set.contains(org.junit.runner.Describable.class));

    assertTrue(set.contains(org.easymock.internal.ILegacyMatcherMethods.class));
    assertTrue(set.contains(org.easymock.internal.ILegacyMethods.class));
    assertTrue(set.contains(org.easymock.internal.IMocksBehavior.class));
    assertTrue(set.contains(org.easymock.internal.IMocksControlState.class));
    assertTrue(set.contains(org.easymock.internal.IProxyFactory.class));
    assertTrue(set.contains(org.easymock.ArgumentsMatcher.class));
    assertTrue(set.contains(org.easymock.IAnswer.class));
    assertTrue(set.contains(org.easymock.IArgumentMatcher.class));
    assertTrue(set.contains(org.easymock.IExpectationSetters.class));
    assertTrue(set.contains(org.easymock.IMocksControl.class));

    assertTrue(set.contains(org.easymock.CaptureType.class));
    assertTrue(set.contains(org.easymock.LogicalOperator.class));
  }

}
