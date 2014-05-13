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

package org.atatec.trugger.test.validation;

import org.atatec.trugger.validation.Validation;
import org.atatec.trugger.validation.ValidationResult;
import org.atatec.trugger.validation.validator.NotEmpty;
import org.atatec.trugger.validation.validator.NotNull;
import org.atatec.trugger.validation.validator.Valid;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Marcelo Guimarães
 */
public class ValidValidationTest extends BaseValidatorTest<Valid> {

  public static class Project {

    @NotNull
    @NotEmpty
    String name;

    @NotNull
    @NotEmpty
    String author;

    public Project(String author, String name) {
      this.author = author;
      this.name = name;
    }
  }

  @Valid
  private List<Project> projects = new ArrayList<>();

  @Test
  public void test() {
    projects.add(new Project("Marcelo", "Trugger"));
    projects.add(new Project(null, ""));
    projects.add(new Project("Marcelo", "Robobundle"));
    projects.add(new Project("Marcelo", "Soda"));

    ValidationResult result = Validation.engine().validate(this);

    assertTrue(result.isInvalid());

    assertTrue(result.isElementInvalid("projects"));
    assertTrue(result.isElementInvalid("projects.1"));
    assertTrue(result.isElementInvalid("projects.1.name"));
    assertTrue(result.isElementInvalid("projects.1.author"));

    assertNull(result.invalidElement("projects.1.author").get());
    assertEquals("", result.invalidElement("projects.1.name").get());

    testResultElements(result);

    result = Validation.engine().validate(projects);

    assertTrue(result.isInvalid());

    assertTrue(result.isElementInvalid("1"));
    assertTrue(result.isElementInvalid("1.name"));
    assertTrue(result.isElementInvalid("1.author"));

    testResultElements(result);

    result = Validation.engine().validate(projects.toArray());

    assertTrue(result.isInvalid());

    assertTrue(result.isElementInvalid("1"));
    assertTrue(result.isElementInvalid("1.name"));
    assertTrue(result.isElementInvalid("1.author"));

    testResultElements(result);
  }

}
