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
package org.atatec.trugger.test.reflection;

import org.atatec.trugger.reflection.Reflector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.atatec.trugger.reflection.Reflection.*;
import static org.easymock.EasyMock.*;

/**
 * A class for testing method reflection by the {@link Reflector}.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class MethodReflectionTest {

  static interface TestInterface {
    void doIt();

    void foo();

    void bar();
  }

  private TestInterface obj;

  @Before
  public void before() {
    obj = createMock(TestInterface.class);
  }

  @After
  public void after() {
    verify(obj);
  }

  @Test
  public void invokerTest() {
    obj.doIt();
    expectLastCall().once();
    replay(obj);
    invoke(method("doIt")).in(obj).withoutArgs();
  }

  @Test
  public void invokerForNoMethodTest() {
    replay(obj);
    invoke(method("notDeclared")).in(obj).withoutArgs();
  }

}
