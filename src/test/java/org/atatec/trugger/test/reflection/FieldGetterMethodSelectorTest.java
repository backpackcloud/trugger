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

import org.atatec.trugger.test.Flag;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.lang.reflect.Field;

import static org.atatec.trugger.reflection.Reflection.reflect;
import static org.atatec.trugger.reflection.MethodPredicates.getterOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class FieldGetterMethodSelectorTest {

  private int count;
  @Flag
  private int hits;

  @Flag
  @Resource
  public int getCount() {
    return count;
  }

  public int getHits() {
    return hits;
  }

  private Field fieldCount;
  private Field fieldHits;

  @Before
  public void initialize() {
    fieldCount = reflect().field("count").in(this);
    fieldHits = reflect().field("hits").in(this);
  }

  @Test
  public void test() {
    assertFalse(
        reflect().methods()
            .filter(getterOf(fieldCount))
            .in(this)
            .isEmpty()
    );
    assertFalse(
        reflect().methods()
            .filter(getterOf(fieldHits))
            .in(this)
            .isEmpty()
    );
    assertFalse(
        reflect().methods()
            .filter(getterOf("count"))
            .in(this)
            .isEmpty()
    );
    assertFalse(
        reflect().methods()
            .filter(getterOf("hits"))
            .in(this)
            .isEmpty()
    );
    assertTrue(
        reflect().methods()
            .filter(getterOf(fieldCount))
            .in(Object.class)
            .isEmpty()
    );
    assertTrue(
        reflect().methods()
            .filter(getterOf(fieldHits))
            .in(Object.class)
            .isEmpty()
    );
  }

}
