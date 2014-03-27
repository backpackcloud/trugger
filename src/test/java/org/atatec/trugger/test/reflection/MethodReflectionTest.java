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

import org.atatec.trugger.reflection.ReflectionException;
import org.atatec.trugger.reflection.Reflector;
import org.atatec.trugger.test.Flag;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.atatec.trugger.reflection.MethodPredicates.ANNOTATED;
import static org.atatec.trugger.reflection.MethodPredicates.annotatedWith;
import static org.atatec.trugger.reflection.Reflection.invoke;
import static org.atatec.trugger.reflection.Reflection.method;
import static org.atatec.trugger.test.TruggerTest.assertThrow;
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

    @Flag
    void bar();

  }

  @Test
  public void invokerTest() {
    TestInterface obj = createMock(TestInterface.class);
    obj.doIt();
    expectLastCall().once();
    replay(obj);
    invoke(method("doIt")).in(obj).withoutArgs();
    verify(obj);
  }

  @Test
  public void invokerExceptionHandlerTest() {
    TestInterface obj = createMock(TestInterface.class);
    obj.doIt();
    expectLastCall().andThrow(new IllegalArgumentException());
    replay(obj);
    assertThrow(ReflectionException.class,
        () -> invoke(method("doIt")).in(obj).withoutArgs()
    );

    assertThrow(NullPointerException.class,
        () -> invoke(method("doIt"))
            .in(obj)
            .onError(e -> { throw new NullPointerException();})
            .withoutArgs()
    );
    verify(obj);
  }

  @Test
  public void invokerForNoMethodTest() {
    TestInterface obj = createMock(TestInterface.class);
    replay(obj);
    invoke(method("notDeclared")).in(obj).withoutArgs();
    verify(obj);
  }

  @Test
  public void predicatesTest() {
    assertFalse(
        ANNOTATED.test(
            method("doIt").in(TestInterface.class)
        )
    );
    assertFalse(
        annotatedWith(Flag.class).test(
            method("doIt").in(TestInterface.class)
        )
    );
    assertTrue(
        ANNOTATED.test(
            method("bar").in(TestInterface.class)
        )
    );
    assertTrue(
        annotatedWith(Flag.class).test(
            method("bar").in(TestInterface.class)
        )
    );
  }

}
