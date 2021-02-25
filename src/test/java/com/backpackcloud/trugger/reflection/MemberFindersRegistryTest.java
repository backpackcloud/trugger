/*
 * The Apache License
 *
 * Copyright 2009 Marcelo Guimaraes <ataxexe@backpackcloud.com>
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

package com.backpackcloud.trugger.reflection;

import org.junit.Test;
import com.backpackcloud.trugger.reflection.impl.DeclaredMemberFindersRegistry;
import com.backpackcloud.trugger.reflection.impl.MemberFindersRegistry;
import com.backpackcloud.trugger.reflection.impl.VisibleMemberFindersRegistry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/** @author Marcelo Guimaraes */
public class MemberFindersRegistryTest {

  static class TestClass {

    private int privateField;
    protected int protectedField;
    int packageField;
    public int publicField;

    private TestClass(int arg) {
    }

    public TestClass() {
    }

    protected TestClass(String arg) {
    }

    TestClass(boolean arg) {
    }

    private void foo(int arg) {
    }

    public void foo() {
    }

    protected void foo(String arg) {
    }

    void foo(boolean arg) {
    }

  }

  private MemberFindersRegistry declared = new DeclaredMemberFindersRegistry();
  private MemberFindersRegistry visible = new VisibleMemberFindersRegistry();

  @Test
  public void testFields() throws Exception {
    assertEquals(4, declared.fieldsFinder().find(TestClass.class).size());
    assertEquals(1, visible.fieldsFinder().find(TestClass.class).size());

    assertNotNull(declared.fieldFinder("privateField").find(TestClass.class));
    assertNotNull(declared.fieldFinder("protectedField").find(TestClass.class));
    assertNotNull(declared.fieldFinder("packageField").find(TestClass.class));
    assertNotNull(declared.fieldFinder("publicField").find(TestClass.class));

    assertNotNull(visible.fieldFinder("publicField").find(TestClass.class));
  }

  @Test(expected = NoSuchFieldException.class)
  public void testPrivateFieldOnVisibleFinder() throws Exception {
    visible.fieldFinder("privateField").find(TestClass.class);
  }

  @Test(expected = NoSuchFieldException.class)
  public void testProtectedFieldOnVisibleFinder() throws Exception {
    visible.fieldFinder("protectedField").find(TestClass.class);
  }

  @Test(expected = NoSuchFieldException.class)
  public void testPackageFieldOnVisibleFinder() throws Exception {
    visible.fieldFinder("packageField").find(TestClass.class);
  }

  @Test
  public void testConstructors() throws Exception {
    assertEquals(4, declared.constructorsFinder().find(TestClass.class).size());
    assertEquals(1, visible.constructorsFinder().find(TestClass.class).size());

    assertNotNull(visible.constructorFinder().find(TestClass.class));

    assertNotNull(declared.constructorFinder().find(TestClass.class));
    assertNotNull(declared.constructorFinder(int.class).find(TestClass.class));
    assertNotNull(declared.constructorFinder(String.class).find(TestClass.class));
    assertNotNull(declared.constructorFinder(boolean.class).find(TestClass.class));
  }

  @Test(expected = NoSuchMethodException.class)
  public void testPrivateConstructorOnVisibleFinder() throws Exception {
    visible.constructorFinder(int.class).find(TestClass.class);
  }

  @Test(expected = NoSuchMethodException.class)
  public void testProtectedConstructorOnVisibleFinder() throws Exception {
    visible.constructorFinder(String.class).find(TestClass.class);
  }

  @Test(expected = NoSuchMethodException.class)
  public void testPackageConstructorOnVisibleFinder() throws Exception {
    visible.constructorFinder(boolean.class).find(TestClass.class);
  }

  @Test
  public void testMethods() throws Exception {
    assertEquals(4, declared.methodsFinder().find(TestClass.class).size());
    /*
    1 - foo()
    2 - wait(long)
    3 - wait(long,int)
    4 - wait()
    5 - equals(Object)
    6 - toString()
    7 - hashCode()
    8 - getClass()
    9 - Object.notify()
   10 - notifyAll()
    */
    assertEquals(10, visible.methodsFinder().find(TestClass.class).size());

    assertNotNull(visible.methodFinder("foo").find(TestClass.class));

    assertNotNull(declared.methodFinder("foo").find(TestClass.class));
    assertNotNull(declared.methodFinder("foo", int.class).find(TestClass.class));
    assertNotNull(declared.methodFinder("foo", String.class).find(TestClass.class));
    assertNotNull(declared.methodFinder("foo", boolean.class).find(TestClass.class));
  }

  @Test(expected = NoSuchMethodException.class)
  public void testPrivateMethodOnVisibleFinder() throws Exception {
    visible.methodFinder("foo", int.class).find(TestClass.class);
  }

  @Test(expected = NoSuchMethodException.class)
  public void testProtectedMethodOnVisibleFinder() throws Exception {
    visible.methodFinder("foo", String.class).find(TestClass.class);
  }

  @Test(expected = NoSuchMethodException.class)
  public void testPackageMethodOnVisibleFinder() throws Exception {
    visible.methodFinder("foo", boolean.class).find(TestClass.class);
  }

}
