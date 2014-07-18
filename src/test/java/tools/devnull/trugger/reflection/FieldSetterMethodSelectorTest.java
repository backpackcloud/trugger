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
package tools.devnull.trugger.reflection;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.trugger.Flag;

import javax.annotation.Resource;
import java.lang.reflect.Field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static tools.devnull.trugger.reflection.MethodPredicates.setterOf;
import static tools.devnull.trugger.reflection.Reflection.field;
import static tools.devnull.trugger.reflection.Reflection.reflect;

/**
 * @author Marcelo Varella Barca Guimarães
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
    fieldCount = field("count").in(this);
    fieldHits = field("hits").in(this);
    fieldSize = field("size").in(this);
  }

  @Test
  public void test() {
    assertFalse(
        reflect().methods()
            .filter(setterOf(fieldHits))
            .in(this)
            .isEmpty()
    );
    assertFalse(
        reflect().methods()
            .filter(setterOf(fieldCount))
            .in(this)
            .isEmpty()
    );
    assertTrue(
        reflect().methods()
            .filter(setterOf(fieldHits))
            .in(Object.class)
            .isEmpty()
    );
    assertTrue(
        reflect().methods()
            .filter(setterOf(fieldCount))
            .in(Object.class)
            .isEmpty()
    );
  }

}
