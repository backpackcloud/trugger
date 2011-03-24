/*
 * Copyright 2009-2011 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.validation.validator;


import net.sf.trugger.validation.ValidRegion;
import net.sf.trugger.validation.Validator;

/**
 * Implementation of the {@link Range} validation.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.1
 */
public class RangeValidator implements Validator<Number> {

  private Range range;

  public boolean isValid(@NotNull Number numberValue) {
    double min = range.min();
    double max = range.max();
    double value = numberValue.doubleValue();
    double delta = range.delta();
    boolean out = range.validRegion() == ValidRegion.OUT;
    boolean result;

    if (out) {
      result = (value - delta < min) || (value + delta > max);
    } else {
      result = (value + delta > min) && (value - delta < max);
    }

    if (!result) {
      if (range.includeMin()) {
        result = areEquals(value, min, delta);
      }
      if (!result) {
        if (range.includeMax()) {
          result = areEquals(value, max, delta);
        }
      }
    }
    return result;
  }

  private static boolean areEquals(double x, double y, double delta) {
    return Math.abs(x - y) <= delta;
  }
}
