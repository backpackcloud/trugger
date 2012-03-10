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
package net.sf.trugger.transformer;

import net.sf.trugger.element.ElementCopy;
import net.sf.trugger.util.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A class that holds a set of useful transformers for common use.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.6
 */
public final class Transformers {

  private Transformers() {
  }

  /** Transformer from Object to Boolean */
  public static final Transformer<Boolean, Object> TO_BOOLEAN = new Transformer<Boolean, Object>() {

    @Override
    public Boolean transform(Object object) {
      return Boolean.valueOf(String.valueOf(object));
    }
  };

  /** Transformer from Object to Double */
  public static final Transformer<Double, Object> TO_DOUBLE = new Transformer<Double, Object>() {

    @Override
    public Double transform(Object object) {
      return Double.valueOf(String.valueOf(object));
    }
  };

  /** Transformer from Object to Float */
  public static final Transformer<Float, Object> TO_FLOAT = new Transformer<Float, Object>() {

    @Override
    public Float transform(Object object) {
      return Float.valueOf(String.valueOf(object));
    }
  };

  /** Transformer from Object to Integer */
  public static final Transformer<Integer, Object> TO_INTEGER = new Transformer<Integer, Object>() {

    @Override
    public Integer transform(Object object) {
      return Integer.valueOf(String.valueOf(object));
    }
  };

  /** Transformer from Object to Long */
  public static final Transformer<Long, Object> TO_LONG = new Transformer<Long, Object>() {

    @Override
    public Long transform(Object object) {
      return Long.valueOf(String.valueOf(object));
    }
  };

  /** Transformer from Object to String */
  public static final Transformer<String, Object> TO_STRING = new Transformer<String, Object>() {

    @Override
    public String transform(Object object) {
      return String.valueOf(object);
    }
  };

  private static final Map<Class, Transformer> TRANSFORMERS;

  static {
    TRANSFORMERS = new HashMap<Class, Transformer>();
    TRANSFORMERS.put(Boolean.class, TO_BOOLEAN);
    TRANSFORMERS.put(Integer.class, TO_INTEGER);
    TRANSFORMERS.put(Long.class, TO_LONG);
    TRANSFORMERS.put(Float.class, TO_FLOAT);
    TRANSFORMERS.put(Double.class, TO_DOUBLE);
    TRANSFORMERS.put(String.class, TO_STRING);
  }

  /**
   * An useful transformer to copy {@link Properties} elements.
   * <p/>
   * Elements of the types below will be converted automaticaly: <ul> <li>String
   * <li>Integer or int <li>Long or long <li>Float or float <li>Double or double
   * <li>Boolean or boolean </ul>
   */
  public static Transformer<Object, ElementCopy> properties() {
    return new Transformer<Object, ElementCopy>() {

      @Override
      public Object transform(ElementCopy object) {
        Class type = Utils.objectClass(object.destinationElement().type());
        if (TRANSFORMERS.containsKey(type)) {
          return TRANSFORMERS.get(type).transform(object.value());
        }
        return object.value();
      }

    };
  }
}
