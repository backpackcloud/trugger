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
package io.backpackcloud.trugger.element.impl;

import io.backpackcloud.trugger.Optional;
import io.backpackcloud.trugger.TruggerException;
import io.backpackcloud.trugger.element.Element;
import io.backpackcloud.trugger.element.ElementFinder;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Marcelo "Ataxexe" Guimarães
 */
public class ResultSetElementFinder implements ElementFinder {

  @Override
  public boolean canFind(Class type) {
    return ResultSet.class.isAssignableFrom(type);
  }

  @Override
  public List<Element> findAll(Object target) {
    if (target instanceof Class<?>) {
      return Collections.emptyList();
    }
    List<Element> elements = new ArrayList<>();
    ResultSet resultSet = (ResultSet) target;
    try {
      ResultSetMetaData metaData = resultSet.getMetaData();
      for (int i = 1; i <= metaData.getColumnCount(); i++) {
        elements.add(
            new SpecificElement(
                new ResultSetElement(metaData.getColumnName(i)), resultSet)
        );
      }
    } catch (SQLException e) {
      throw new TruggerException(e);
    }
    return elements;
  }

  @Override
  public Optional<Element> find(String name, Object target) {
    if (target instanceof Class<?>) {
      return Optional.of(new ResultSetElement(name));
    }
    return Optional.of(new SpecificElement(new ResultSetElement(name), target));
  }

}
