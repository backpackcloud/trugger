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
package io.backpackcloud.trugger.reflection;

import org.junit.Before;
import org.junit.Test;
import io.backpackcloud.trugger.Flag;

import javax.annotation.Resource;
import java.lang.reflect.Field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static io.backpackcloud.trugger.reflection.MethodPredicates.setterOf;

/**
 * @author Marcelo Guimaraes
 */
public class FieldSetterMethodSelectorTest {

  private int count;
  private int hits;
  private int size;

  @Flag
  @Resource
  public void setCount(int count) {
    this.count = count;
  }

  public void setHits(int hits) {
    this.hits = hits;
  }

  @Resource
  public void setSize(int size) {
    this.size = size;
  }

  private Field fieldCount;
  private Field fieldHits;
  private Field fieldSize;

  @Before
  public void initialize() {
    fieldCount = Reflection.reflect().field("count").from(this).map(ReflectedField::unwrap).get();
    fieldHits = Reflection.reflect().field("hits").from(this).map(ReflectedField::unwrap).get();
    fieldSize = Reflection.reflect().field("size").from(this).map(ReflectedField::unwrap).get();
  }

  @Test
  public void test() {
    assertFalse(
        Reflection.reflect().methods()
            .filter(setterOf(fieldHits))
            .from(this)
            .isEmpty()
    );
    assertFalse(
        Reflection.reflect().methods()
            .filter(setterOf(fieldCount))
            .from(this)
            .isEmpty()
    );
    assertTrue(
        Reflection.reflect().methods()
            .filter(setterOf(fieldHits))
            .from(Object.class)
            .isEmpty()
    );
    assertTrue(
        Reflection.reflect().methods()
            .filter(setterOf(fieldCount))
            .from(Object.class)
            .isEmpty()
    );
  }

}
