/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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
package tools.devnull.trugger.reflection;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.trugger.Flag;

import javax.annotation.Resource;
import java.lang.reflect.Field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static tools.devnull.trugger.reflection.MethodPredicates.getterOf;
import static tools.devnull.trugger.reflection.Reflection.reflect;

/**
 * @author Marcelo "Ataxexe" Guimarães
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
    fieldCount = reflect().field("count").from(this).result();
    fieldHits = reflect().field("hits").from(this).result();
  }

  @Test
  public void test() {
    assertFalse(
        reflect().methods()
            .filter(getterOf(fieldCount))
            .from(this)
            .isEmpty()
    );
    assertFalse(
        reflect().methods()
            .filter(getterOf(fieldHits))
            .from(this)
            .isEmpty()
    );
    assertFalse(
        reflect().methods()
            .filter(getterOf("count"))
            .from(this)
            .isEmpty()
    );
    assertFalse(
        reflect().methods()
            .filter(getterOf("hits"))
            .from(this)
            .isEmpty()
    );
    assertTrue(
        reflect().methods()
            .filter(getterOf(fieldCount))
            .from(Object.class)
            .isEmpty()
    );
    assertTrue(
        reflect().methods()
            .filter(getterOf(fieldHits))
            .from(Object.class)
            .isEmpty()
    );
  }

}
