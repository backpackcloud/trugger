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
package org.atatec.trugger.test.predicate;

import org.atatec.trugger.predicate.CompositePredicate;
import org.atatec.trugger.predicate.Predicate;
import org.atatec.trugger.predicate.Predicates;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.atatec.trugger.predicate.Predicates.not;

/**
 * A class for testing the {@link Predicates} class.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class PredicatesTest {

  private CompositePredicate<Object> T;
  private CompositePredicate<Object> F;

  private boolean eval(Predicate<?> predicate) {
    return predicate.evaluate(null);
  }

  @Before
  public void initialize() {
    T = Predicates.ALWAYS_TRUE;
    F = Predicates.ALWAYS_FALSE;
  }

  @Test
  public void testBasicOperations() {
    assertFalse(eval(not(T)));
    assertTrue(eval(not(F)));

    assertTrue(eval(T.and(T)));
    assertFalse(eval(T.and(F)));

    assertFalse(eval(T.nand(T)));
    assertTrue(eval(F.nand(T)));

    assertTrue(eval(T.or(F)));
    assertFalse(eval(F.or(F)));

    assertFalse(eval(T.nor(F)));
    assertTrue(eval(F.nor(F)));

    assertTrue(eval(T.xor(F)));
    assertFalse(eval(T.xor(T)));

    assertFalse(eval(T.xand(F)));
    assertTrue(eval(F.xand(F)));

    assertFalse(eval(not(new Predicate() {

      public boolean evaluate(Object element) {
        return true;
      }
    })));
  }

  @Test
  public void testFormulas() {
    // (F && !T) || (!F && T) || F
    assertTrue(eval(F.and(T.negate()).or(F.negate().and(T)).or(F)));
    // !F && (T || F)
    assertTrue(eval(F.negate().and(T.or(F))));
    // (F && T || F) = F
    assertTrue(eval(F.and(T).or(F).xand(F)));
  }

}
