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

package tools.devnull.trugger.validation;

import tools.devnull.trugger.reflection.Execution;

import java.lang.reflect.Executable;

/**
 * A class to help validating arguments of methods and constructors.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public class ArgumentsValidator {

  private final ValidationEngine engine;

  /**
   * Creates a new instance using the default factory.
   */
  public ArgumentsValidator() {
    this(Validation.engine());
  }

  /**
   * Creates a new instance using the given validator factory.
   *
   * @param engine the engine to use
   */
  public ArgumentsValidator(ValidationEngine engine) {
    this.engine = engine;
  }

  /**
   * Checks if the arguments are valid using the annotations defined in the
   * parameters.
   *
   * @param executable the executable to extract the parameters
   * @param args       the arguments passed
   * @return <code>true</code> if all arguments are valid or none of the
   * parameters has constraints.
   */
  public boolean isValid(Executable executable, Object... args) {
    return validate(executable, args).isValid();
  }

  /**
   * Validates the given execution of an {@link Executable}.
   *
   * @param executable the executable object (a method or a constructor)
   * @param args       the actual args that will be passed to the given executable
   * @return the validation result
   * @see Execution
   * @since 5.2
   */
  public ValidationResult validate(Executable executable, Object... args) {
    return engine.validate(new Execution(executable, args));
  }

}
