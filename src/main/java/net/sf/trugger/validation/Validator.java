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
package net.sf.trugger.validation;

import java.lang.annotation.Annotation;
import java.util.Properties;

/**
 * Interface that defines a Validator for a value.
 * <p>
 * The validator can use constraints to do the job. They should be based on
 * specific resources defined by the Validator (such as XML files,
 * {@link Properties} object or {@link Annotation Annotations}).
 * 
 * @author Marcelo Varella Barca Guimarães
 * @param <T>
 *          The type of the value.
 */
public interface Validator<T> {
  
  /**
   * Validates the specified value.
   * 
   * @param value
   *          the value to be validated.
   * @return <code>true</code> if, and only if, the given value is valid.
   */
  public boolean isValid(T value);
  
}
