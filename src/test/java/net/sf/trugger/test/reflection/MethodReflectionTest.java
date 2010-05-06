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
package net.sf.trugger.test.reflection;

import static net.sf.trugger.reflection.Reflection.invoke;
import static net.sf.trugger.reflection.Reflection.reflect;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.lang.reflect.Method;
import java.util.Set;

import net.sf.trugger.reflection.Reflector;

import org.junit.Test;

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

  @Test
  public void invokerTest() {
    TestInterface obj = createMock(TestInterface.class);
    obj.doIt();
    expectLastCall().once();
    replay(obj);
    Method method = reflect().method("doIt").in(TestInterface.class);
    invoke(method).in(obj).withoutArgs();
    verify(obj);
  }

  @Test
  public void invokerForCollectionTest() {
    TestInterface obj = createMock(TestInterface.class);
    obj.doIt();
    expectLastCall().once();
    obj.foo();
    expectLastCall().once();
    obj.bar();
    expectLastCall().once();
    replay(obj);
    Set<Method> methods = reflect().methods().in(TestInterface.class);
    invoke(methods).in(obj).withoutArgs();
    verify(obj);
  }

}
