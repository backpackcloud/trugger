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
package net.sf.trugger.test.predicate;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static net.sf.trugger.predicate.Predicates.newComposition;
import static net.sf.trugger.predicate.Predicates.not;
import static net.sf.trugger.predicate.Predicates.valueOf;
import net.sf.trugger.predicate.CompositePredicate;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.predicate.Predicates;

import org.junit.Before;
import org.junit.Test;

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
    T = Predicates.alwaysTrue();
    F = Predicates.alwaysFalse();
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

    assertTrue(eval(valueOf(true)));
    assertFalse(eval(valueOf(false)));

    assertFalse(eval(not(newComposition(new Predicate() {

      public boolean evaluate(Object element) {
        return true;
      }
    }))));
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
