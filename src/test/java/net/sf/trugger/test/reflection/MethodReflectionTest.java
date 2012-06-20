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
package net.sf.trugger.test.reflection;

import net.sf.trugger.reflection.Reflector;
import net.sf.trugger.test.Flag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static net.sf.trugger.reflection.Reflection.invoke;
import static net.sf.trugger.reflection.Reflection.method;
import static net.sf.trugger.reflection.Reflection.methods;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

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
  public void invokerForCollectionTest() {
    obj.doIt();
    expectLastCall().once();
    obj.foo();
    expectLastCall().once();
    obj.bar();
    expectLastCall().once();
    replay(obj);
    invoke(methods().in(TestInterface.class)).in(obj).withoutArgs();
  }

  @Test
  public void invokerForNoMethodTest() {
    replay(obj);
    invoke(method("notDeclared")).in(obj).withoutArgs();
    invoke(methods().annotatedWith(Flag.class)).in(TestInterface.class).withoutArgs();
  }

}
