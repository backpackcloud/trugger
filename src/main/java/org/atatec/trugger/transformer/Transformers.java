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
package org.atatec.trugger.transformer;

import org.atatec.trugger.element.ElementCopy;
import org.atatec.trugger.util.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A class that holds a set of useful transformers for common use.
 *
 * @author Marcelo Guimarães
 * @since 2.6
 */
public final class Transformers {

  private Transformers() {
  }

  /** Transformer from Object to Boolean */
  public static final Transformer<Boolean, Object> BOOLEAN =
    object -> Boolean.valueOf(String.valueOf(object));

  /** Transformer from Object to Double */
  public static final Transformer<Double, Object> DOUBLE =
    object -> Double.valueOf(String.valueOf(object));

  /** Transformer from Object to Float */
  public static final Transformer<Float, Object> FLOAT =
    object -> Float.valueOf(String.valueOf(object));

  /** Transformer from Object to Integer */
  public static final Transformer<Integer, Object> INTEGER =
    object -> Integer.valueOf(String.valueOf(object));

  /** Transformer from Object to Long */
  public static final Transformer<Long, Object> LONG =
    object -> Long.valueOf(String.valueOf(object));

  /** Transformer from Object to String */
  public static final Transformer<String, Object> STRING =
    object -> String.valueOf(object);

  /** Transformer from Object to Byte */
  public static final Transformer<Byte, Object> BYTE =
    object -> Byte.valueOf(String.valueOf(object));

  /** Transformer from Object to Short */
  public static final Transformer<Short, Object> SHORT =
    object -> Short.valueOf(String.valueOf(object));

  /** Transformer from Object to Character */
  public static final Transformer<Character, Object> CHAR =
    object -> String.valueOf(object).charAt(0);

  private static final Map<Class, Transformer> TRANSFORMERS;

  static {
    TRANSFORMERS = new HashMap<>();
    TRANSFORMERS.put(Boolean.class, BOOLEAN);
    TRANSFORMERS.put(Integer.class, INTEGER);
    TRANSFORMERS.put(Long.class, LONG);
    TRANSFORMERS.put(Float.class, FLOAT);
    TRANSFORMERS.put(Double.class, DOUBLE);
    TRANSFORMERS.put(String.class, STRING);
    TRANSFORMERS.put(Short.class, SHORT);
    TRANSFORMERS.put(Character.class, CHAR);
    TRANSFORMERS.put(Byte.class, BYTE);
  }

  /**
   * An useful transformer to copy {@link Properties} elements.
   */
  public static final Transformer<Object, ElementCopy> PROPERTIES = new Transformer<Object, ElementCopy>() {

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
