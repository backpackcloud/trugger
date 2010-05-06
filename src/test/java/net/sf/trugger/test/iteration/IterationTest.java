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

package net.sf.trugger.test.iteration;

import static junit.framework.Assert.assertNotNull;
import static net.sf.trugger.iteration.Iteration.copyTo;
import static net.sf.trugger.iteration.Iteration.countIn;
import static net.sf.trugger.iteration.Iteration.moveTo;
import static net.sf.trugger.iteration.Iteration.removeFrom;
import static net.sf.trugger.iteration.Iteration.retainFrom;
import static net.sf.trugger.iteration.Iteration.selectFrom;
import static net.sf.trugger.test.TruggerTest.assertMatch;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.trugger.Transformer;
import net.sf.trugger.iteration.Iteration;
import net.sf.trugger.iteration.SearchException;
import net.sf.trugger.predicate.CompositePredicate;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.predicate.Predicates;

import org.junit.Before;
import org.junit.Test;

/**
 * A class for testing the {@link Iteration} class.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class IterationTest {

  private CompositePredicate<Integer> even = Predicates.newComposition(new Predicate<Integer>() {

    public boolean evaluate(Integer element) {
      return element % 2 == 0;
    }
  });
  private CompositePredicate<Integer> odd = even.negate();

  private Collection<Integer> numbers;
  private int initialNumbersSize;
  private int initialNumbersHalfSize;

  private Transformer<String, Integer> transformer = new Transformer<String, Integer>() {

    public String transform(Integer object) {
      return object.toString();
    }
  };

  @Before
  public void initialize() {
    numbers = new ArrayList<Integer>();
    for (int i = 0 ; i < 10000 ; i++) {
      numbers.add(i);
    }
    initialNumbersSize = numbers.size();
    initialNumbersHalfSize = initialNumbersSize / 2;
  }

  @Test
  public void retainTest() {
    int count = countIn(numbers).elementsMatching(even);
    int result = retainFrom(numbers).elementsMatching(even);
    assertEquals(initialNumbersHalfSize, count);
    assertEquals(count, result);
    assertEquals(initialNumbersHalfSize, numbers.size());
    assertMatch(numbers, even);
  }

  @Test
  public void removeTest() {
    int count = countIn(numbers).elementsMatching(even);
    int result = removeFrom(numbers).elementsMatching(even);
    assertEquals(initialNumbersHalfSize, count);
    assertEquals(count, result);
    assertEquals(initialNumbersHalfSize, numbers.size());
    assertMatch(numbers, odd);
  }

  @Test
  public void predicateCopyTest() {
    Collection<Integer> evenNumbers = new ArrayList<Integer>();
    int result = copyTo(evenNumbers).elementsMatching(even).from(numbers);
    assertEquals(initialNumbersHalfSize, result);
    assertEquals(initialNumbersHalfSize, evenNumbers.size());
    assertMatch(evenNumbers, even);

    assertEquals(initialNumbersSize, numbers.size());
  }

  @Test
  public void predicateMoveTest() {
    Collection<Integer> evenNumbers = new ArrayList<Integer>();
    int result = moveTo(evenNumbers).elementsMatching(even).from(numbers);
    assertEquals(initialNumbersHalfSize, result);
    assertEquals(initialNumbersHalfSize, evenNumbers.size());
    assertMatch(evenNumbers, even);

    assertEquals(initialNumbersHalfSize, numbers.size());
    assertMatch(numbers, odd);
  }

  @Test
  public void copyTest() {
    Collection<Integer> integers = new ArrayList<Integer>();
    int result = copyTo(integers).allElements().from(numbers);
    assertEquals(initialNumbersSize, result);
    assertEquals(initialNumbersSize, integers.size());
    assertEquals(initialNumbersSize, numbers.size());
  }

  @Test
  public void moveTest() {
    Collection<Integer> integers = new ArrayList<Integer>();
    int result = moveTo(integers).allElements().from(numbers);
    assertEquals(initialNumbersSize, result);
    assertEquals(initialNumbersSize, integers.size());
    assertEquals(0, numbers.size());
  }

  @Test
  public void transformCopyTest() {
    Collection<String> strings = new ArrayList<String>();
    int result = copyTo(strings).transformingWith(transformer).allElements().from(numbers);
    assertEquals(initialNumbersSize, result);
    assertEquals(initialNumbersSize, strings.size());
    assertEquals(initialNumbersSize, numbers.size());
  }

  @Test
  public void predicateTransformCopyTest() {
    Collection<String> strings = new ArrayList<String>();
    int result = copyTo(strings).transformingWith(transformer).elementsMatching(even).from(numbers);
    assertEquals(initialNumbersHalfSize, result);
    assertEquals(initialNumbersHalfSize, strings.size());
    assertEquals(initialNumbersSize, numbers.size());
  }

  @Test
  public void transformMoveTest() {
    Collection<String> strings = new ArrayList<String>();
    int result = moveTo(strings).transformingWith(transformer).allElements().from(numbers);
    assertEquals(initialNumbersSize, result);
    assertEquals(initialNumbersSize, strings.size());
    assertEquals(0, numbers.size());
  }

  @Test
  public void predicateTransformMoveTest() {
    Collection<String> strings = new ArrayList<String>();
    int result = moveTo(strings).transformingWith(transformer).elementsMatching(even).from(numbers);
    assertEquals(initialNumbersHalfSize, result);
    assertEquals(initialNumbersHalfSize, strings.size());
    assertEquals(initialNumbersHalfSize, numbers.size());
  }

  @Test
  public void singleElementSearchTest() {
    Integer one = selectFrom(numbers).elementMatching(new Predicate<Integer>() {
      public boolean evaluate(Integer element) {
        return element.equals(1);
      }
    });
    assertNotNull(one);
  }

  @Test(expected = SearchException.class)
  public void singleElementFailSearchTest() {
    selectFrom(numbers).elementMatching(even);
  }

  @Test
  public void firstElementSearchTest() {
    assertEquals(Integer.valueOf(0), selectFrom(numbers).firstElementMatching(even));
  }

  @Test
  public void multipleElementSearchTest() {
    List<Integer> result = selectFrom(numbers).elementsMatching(odd);
    assertEquals(initialNumbersHalfSize, result.size());
    assertMatch(result, odd);
  }

}
