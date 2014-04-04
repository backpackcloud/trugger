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
package org.atatec.trugger.element.impl;

import org.atatec.trugger.Finder;
import org.atatec.trugger.Result;
import org.atatec.trugger.TruggerException;
import org.atatec.trugger.element.Element;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Marcelo Guimarães
 */
public class ResultSetElementFinder implements Finder<Element> {

  @Override
  public Result<List<Element>, Object> findAll() {
    return target -> {
      if (target instanceof Class<?>) {
        return Collections.emptyList();
      }
      List<Element> elements = new ArrayList<>();
      ResultSet resultSet = (ResultSet) target;
      try {
        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int i = 1 ; i <= metaData.getColumnCount() ; i++) {
          elements.add(
              new SpecificElement(
                  new ResultSetElement(metaData.getColumnName(i)), resultSet)
          );
        }
      } catch (SQLException e) {
        throw new TruggerException(e);
      }
      return elements;
    };
  }

  @Override
  public Result<Element, Object> find(final String name) {
    return target -> {
      if (target instanceof Class<?>) {
        return new ResultSetElement(name);
      }
      return new SpecificElement(new ResultSetElement(name), target);
    };
  }

}
