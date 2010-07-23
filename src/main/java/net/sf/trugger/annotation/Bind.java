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
package net.sf.trugger.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Denotes a bind to the annotated element. If it is a class, the bind must be
 * done for all elements.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Bind {

  /**
   * The element name for bind. Defauls to the annotated element name.
   * <p>
   * Don't use this property if the annotated element is a class.
   */
  String to() default "";

}
