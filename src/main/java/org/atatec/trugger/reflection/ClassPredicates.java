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

package org.atatec.trugger.reflection;

import org.atatec.trugger.predicate.CompositePredicate;
import org.atatec.trugger.predicate.Predicate;
import org.atatec.trugger.predicate.Predicates;

import java.util.Arrays;

import static org.atatec.trugger.predicate.Predicates.is;

/**
 * A set of predicates to use with <code>Class</code> objects.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 4.1
 */
public class ClassPredicates {

  private ClassPredicates() {
  }

  /**
   * Predicate for instantiable classes. Classes that applies must:
   * <p/>
   * <ul> <li>not be abstract</li> <li>be public</li> <li>have at least one public
   * constructor</li> </ul>
   */
  public static final CompositePredicate<Class> INSTANTIABLE = Predicates.is(new Predicate<Class>() {
    @Override
    public boolean evaluate(Class element) {
      return false;
    }
  });

  /**
   * @return a predicate that returns <code>false</code> if the evaluated class has the
   *         specified modifiers.
   */
  public static CompositePredicate<Class> declare(final int... modifiers) {
    return is(new Predicate<Class>() {

      public boolean evaluate(Class element) {
        int elModifiers = element.getModifiers();
        for (int mod : modifiers) {
          if ((elModifiers & mod) != 0) {
            return true;
          }
        }
        return false;
      }

      @Override
      public String toString() {
        return "Class with " + Arrays.toString(modifiers) + " modifiers";
      }
    });
  }

  /**
   * @return a predicate that returns <code>false</code> if the evaluated class does not
   *         have the specified modifiers.
   */
  public static CompositePredicate<Class> dontDeclare(int... modifiers) {
    return declare(modifiers).negate();
  }

}
