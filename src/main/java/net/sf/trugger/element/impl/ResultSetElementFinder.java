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
package net.sf.trugger.element.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.trugger.Finder;
import net.sf.trugger.Result;
import net.sf.trugger.element.Element;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ResultSetElementFinder implements Finder<Element> {

  @Override
  public Result<Set<Element>, Object> findAll() {
    return new Result<Set<Element>, Object>() {

      @Override
      public Set<Element> in(Object target) {
        if (target instanceof Class<?>) {
          return Collections.emptySet();
        }
        Set<Element> elements = new HashSet<Element>();
        ResultSet resultSet = (ResultSet) target;
        try {
          ResultSetMetaData metaData = resultSet.getMetaData();
          for (int i = 1 ; i <= metaData.getColumnCount() ; i++) {
            elements.add(new SpecificElement(new ResultSetElement(metaData.getColumnName(i)), resultSet));
          }
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
        return elements;
      }
    };
  }

  @Override
  public Result<Element, Object> find(final String name) {
    return new Result<Element, Object>() {

      @Override
      public Element in(Object target) {
        if (target instanceof Class<?>) {
          return new ResultSetElement(name);
        }
        return new SpecificElement(new ResultSetElement(name), target);
      }
    };
  }

}
