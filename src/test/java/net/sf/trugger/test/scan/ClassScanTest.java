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

import static net.sf.trugger.scan.ClassScan.findAll;
import static net.sf.trugger.scan.ClassScan.findAnnotations;
import static net.sf.trugger.scan.ClassScan.findClasses;
import static net.sf.trugger.scan.ClassScan.findEnums;
import static net.sf.trugger.scan.ClassScan.findInterfaces;
import static net.sf.trugger.test.TruggerTest.assertMatch;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.reflection.Access;
import net.sf.trugger.reflection.ReflectionPredicates;
import net.sf.trugger.scan.PackageScan;
import net.sf.trugger.scan.ScanLevel;
import net.sf.trugger.test.Flag;
import net.sf.trugger.test.TruggerTest;

import org.junit.Test;

/**
 * A class for testing the class finding.
 *
 * @author Marcelo Varella
 */
@Flag
public class ClassScanTest {

  private String filePackageName = "net.sf.trugger.test.scan.classes";
  private String[] jarPackageNames = new String[] { "org.junit.runner", "org.easymock" };

  private Predicate<Class<?>> filePackagePredicate = new Predicate<Class<?>>() {

    public boolean evaluate(Class<?> element) {
      return element.getPackage().getName().startsWith(filePackageName);
    }
  };

  private Predicate<Class<?>> jarPackagePredicate = new Predicate<Class<?>>() {

    public boolean evaluate(Class<?> element) {
      String packageName = element.getPackage().getName();
      return packageName.startsWith(jarPackageNames[0]) || packageName.startsWith(jarPackageNames[1]);
    }
  };

  private int interfaceTest(String... packages) {
    Set<Class<?>> interfaces =
        findInterfaces().withAccess(Access.PUBLIC).in(ScanLevel.SUBPACKAGES.createScanPackages(packages));
    TruggerTest.assertMatch(interfaces, ReflectionPredicates.INTERFACE.and(Access.PUBLIC.classPredicate()));
    return interfaces.size();
  }

  private int classesTest(String... packages) {
    Set<Class<?>> classes =
        findClasses().withAccess(Access.PUBLIC).in(ScanLevel.SUBPACKAGES.createScanPackages(packages));
    TruggerTest.assertMatch(classes, ReflectionPredicates.CLASS.and(Access.PUBLIC.classPredicate()));
    return classes.size();
  }

  private int annotationsTest(String... packages) {
    Set<Class<?>> annotations =
        findAnnotations().withAccess(Access.PUBLIC).in(ScanLevel.SUBPACKAGES.createScanPackages(packages));
    TruggerTest.assertMatch(annotations, ReflectionPredicates.ANNOTATION.and(Access.PUBLIC.classPredicate()));
    return annotations.size();
  }

  private int enumsTest(String... packages) {
    Set<Class<?>> enums =
        findEnums().withAccess(Access.PUBLIC).in(ScanLevel.SUBPACKAGES.createScanPackages(packages));
    TruggerTest.assertMatch(enums, ReflectionPredicates.ENUM.and(Access.PUBLIC.classPredicate()));
    return enums.size();
  }

  private int allTest(String... packages) {
    Set<Class<?>> classes =
        findAll().withAccess(Access.PUBLIC).in(ScanLevel.SUBPACKAGES.createScanPackages(packages));
    TruggerTest.assertMatch(classes, Access.PUBLIC.classPredicate());
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
    scanTest("net.sf.trugger");
  }

  @Test
  public void testFindInJar() {
    scanTest("org.junit");
  }

  @Test
  public void testScanLevelInFile() {
    scanLevelTest("net.sf.trugger");
  }

  @Test
  public void testScanLevelInJar() {
    scanLevelTest("org.junit");
  }

  private void scanLevelTest(String packageName) {
    Set<Class<?>> packageClasses = findAll().in(packageName);
    Set<Class<?>> subpackageClasses = findAll().recursively().in(packageName);

    assertTrue(packageClasses.size() < subpackageClasses.size());
  }

  @Test
  public void testEqualsAndHash() {
    PackageScan scan1 = new PackageScan("net.sf.trugger", ScanLevel.PACKAGE);
    PackageScan scan2 = new PackageScan("net.sf.trugger", ScanLevel.SUBPACKAGES);

    assertFalse(scan1.equals(scan2));
    assertFalse(scan1.hashCode() == scan2.hashCode());

    scan1 = new PackageScan("net.sf.trugger", ScanLevel.PACKAGE);
    scan2 = new PackageScan("net.sf", ScanLevel.PACKAGE);

    assertFalse(scan1.equals(scan2));
    assertFalse(scan1.hashCode() == scan2.hashCode());

    scan2 = new PackageScan("net.sf.trugger", ScanLevel.PACKAGE);

    assertTrue(scan1.equals(scan2));
    assertTrue(scan1.hashCode() == scan2.hashCode());
  }

  @Test
  public void testClassScanPacakgeInFile() {
    Set<Class<?>> set = findClasses().in(filePackageName);
    assertEquals(1, set.size());
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.MyClass.class));
    assertMatch(set, filePackagePredicate);
  }

  @Test
  public void testAnnotationScanPacakgeInFile() {
    Set<Class<?>> set = findAnnotations().in(filePackageName);
    assertEquals(1, set.size());
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.MyAnnotation.class));
    assertMatch(set, filePackagePredicate);
  }

  @Test
  public void testEnumScanPacakgeInFile() {
    Set<Class<?>> set = findEnums().in(filePackageName);
    assertEquals(1, set.size());
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.MyEnum.class));
    assertMatch(set, filePackagePredicate);
  }

  @Test
  public void testInterfaceScanPacakgeInFile() {
    Set<Class<?>> set = findInterfaces().in(filePackageName);
    assertEquals(1, set.size());
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.MyInterface.class));
    assertMatch(set, filePackagePredicate);
  }

  @Test
  public void testScanPacakgeInFile() {
    Set<Class<?>> set = findAll().in(filePackageName);
    assertEquals(4, set.size());
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.MyClass.class));
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.MyAnnotation.class));
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.MyEnum.class));
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.MyInterface.class));

    assertMatch(set, filePackagePredicate);
  }

  @Test
  public void testClassScanSubPacakgeInFile() {
    Set<Class<?>> set = findClasses().recursively().in(filePackageName);
    assertEquals(2, set.size());
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.MyClass.class));
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.pack.MyClass2.class));

    assertMatch(set, filePackagePredicate);
  }

  @Test
  public void testAnnotationScanSubPacakgeInFile() {
    Set<Class<?>> set = findAnnotations().recursively().fromHere();
    assertEquals(2, set.size());
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.MyAnnotation.class));
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.pack.MyAnnotation2.class));

    assertMatch(set, filePackagePredicate);
  }

  @Test
  public void testClassEnumSubPacakgeInFile() {
    Set<Class<?>> set = findEnums().recursively().fromHere();
    assertEquals(2, set.size());
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.MyEnum.class));
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.pack.MyEnum2.class));

    assertMatch(set, filePackagePredicate);
  }

  @Test
  public void testInterfaceScanSubPacakgeInFile() {
    Set<Class<?>> set = findInterfaces().recursively().fromHere();
    assertEquals(2, set.size());
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.MyInterface.class));
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.pack.MyInterface2.class));

    assertMatch(set, filePackagePredicate);
  }

  @Test
  public void testScanSubPacakgeInFile() {
    Set<Class<?>> set = findAll().recursively().in(filePackageName);
    assertEquals(8, set.size());
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.MyClass.class));
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.pack.MyClass2.class));
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.MyAnnotation.class));
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.pack.MyAnnotation2.class));
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.MyEnum.class));
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.pack.MyEnum2.class));
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.MyInterface.class));
    assertTrue(set.contains(net.sf.trugger.test.scan.classes.pack.MyInterface2.class));

    assertMatch(set, filePackagePredicate);
  }

  @Test
  public void testClassScanSubPacakgeInJar() {
    Set<Class<?>> set = findClasses().recursively().in(jarPackageNames);

    assertMatch(set, ReflectionPredicates.CLASS);
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
  public void testAnnotationScanSubPacakgeInJar() {
    Set<Class<?>> set = findAnnotations().recursively().in(jarPackageNames);

    assertMatch(set, ReflectionPredicates.ANNOTATION);
    assertMatch(set, jarPackagePredicate);

    assertTrue(set.contains(org.junit.runner.RunWith.class));
  }

  @Test
  public void testInterfaceScanSubPacakgeInJar() {
    Set<Class<?>> set = findInterfaces().recursively().in(jarPackageNames);

    assertMatch(set, ReflectionPredicates.INTERFACE);
    assertMatch(set, jarPackagePredicate);

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
  }

  @Test
  public void testEnumScanSubPacakgeInJar() {
    Set<Class<?>> set = findEnums().recursively().in(jarPackageNames);

    assertMatch(set, ReflectionPredicates.ENUM);
    assertMatch(set, jarPackagePredicate);

    assertTrue(set.contains(org.easymock.CaptureType.class));
    assertTrue(set.contains(org.easymock.LogicalOperator.class));
  }

  @Test
  public void testScanSubPacakgeInJar() {
    Set<Class<?>> set = findAll().recursively().in(jarPackageNames);

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
