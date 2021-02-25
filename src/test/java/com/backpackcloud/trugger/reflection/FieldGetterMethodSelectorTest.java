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

import org.junit.Before;
import org.junit.Test;
import com.backpackcloud.trugger.Flag;

import javax.annotation.Resource;
import java.lang.reflect.Field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.backpackcloud.trugger.reflection.MethodPredicates.getterOf;

/**
 * @author Marcelo Guimaraes
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
    fieldCount = Reflection.reflect().field("count").from(this).map(ReflectedField::unwrap).get();
    fieldHits = Reflection.reflect().field("hits").from(this).map(ReflectedField::unwrap).get();
  }

  @Test
  public void test() {
    assertFalse(
        Reflection.reflect().methods()
            .filter(getterOf(fieldCount))
            .from(this)
            .isEmpty()
    );
    assertFalse(
        Reflection.reflect().methods()
            .filter(getterOf(fieldHits))
            .from(this)
            .isEmpty()
    );
    assertFalse(
        Reflection.reflect().methods()
            .filter(getterOf("count"))
            .from(this)
            .isEmpty()
    );
    assertFalse(
        Reflection.reflect().methods()
            .filter(getterOf("hits"))
            .from(this)
            .isEmpty()
    );
    assertTrue(
        Reflection.reflect().methods()
            .filter(getterOf(fieldCount))
            .from(Object.class)
            .isEmpty()
    );
    assertTrue(
        Reflection.reflect().methods()
            .filter(getterOf(fieldHits))
            .from(Object.class)
            .isEmpty()
    );
  }

}
