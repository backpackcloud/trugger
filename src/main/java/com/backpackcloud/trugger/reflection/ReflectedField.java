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

package com.backpackcloud.trugger.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Interface that defines a field reflected with the use of this framework.
 *
 * @since 7.0
 */
public interface ReflectedField extends ReflectedObject<Field> {

  /**
   * Gets the value of the field on the target instance
   *
   * @return the value of the field on the target instance
   */
  <E> E getValue();

  /**
   * Sets this field value
   *
   * @param newValue
   */
  void setValue(Object newValue);

  /**
   * @see Field#isEnumConstant()
   */
  boolean isEnumConstant();

  /**
   * @see Field#getType()
   */
  Class<?> getType();

  /**
   * @see Field#getGenericType()
   */
  Type getGenericType();

}
