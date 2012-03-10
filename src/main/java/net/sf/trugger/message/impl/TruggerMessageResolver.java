/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.message.impl;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.sf.trugger.element.Element;
import net.sf.trugger.element.Elements;
import net.sf.trugger.message.MessageResolver;
import net.sf.trugger.util.Utils;

/**
 * Default class to create messages. If the property that identifies a message
 * is not present in the reference, the name of the reference type will be used
 * to get a String in the ResourceBundle plus a specified suffix.
 * <p>
 * To get the detail of the message, the same rules are used and the suffix
 * should be different.
 * <p>
 * If no message is found in the ResourceBundle, the returned String depends on
 * the requested String:
 * <ul>
 * <li>Summary - the String used to get the message in the ResourceBundle
 * <li>Detail - an empty String
 * <ul>
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerMessageResolver implements MessageResolver {

  private final String summarySuffix;
  private final String detailSuffix;
  private final String name;

  /**
   * Creates a new MessageResolver using the following parameters:
   * <ul>
   * <li>No suffix for the summary.
   * <li>The suffix "_detail" for the detail.
   * <li>The name "message" for the annotation property.
   * </ul>
   */
  public TruggerMessageResolver() {
    this("", "_detail", "message");
  }

  /**
   * Creates a new MessageResolver using the specified parameters.
   *
   * @param summarySuffix
   *          the suffix for getting the summary.
   * @param detailSuffix
   *          the suffix for getting the detail.
   * @param name
   *          the name of the element for getting the message.
   */
  public TruggerMessageResolver(String summarySuffix, String detailSuffix, String name) {
    this.summarySuffix = summarySuffix;
    this.detailSuffix = detailSuffix;
    this.name = name;
  }

  public String getDetail(Object reference, ResourceBundle bundle) {
    return getString(reference, bundle, detailSuffix, false);
  }

  public String getSummary(Object reference, ResourceBundle bundle) {
    return getString(reference, bundle, summarySuffix, true);
  }

  private String getString(Object reference, ResourceBundle bundle, String suffix, boolean returnValueIfNoFound) {
    Class<?> type = Utils.resolveType(reference);
    String value;
    Element element = Elements.element(name).in(type);

    if (element != null) {
      value = element.in(reference).value();
    } else {
      value = type.getName();
    }

    try {
      return bundle.getString(Utils.isEmpty(suffix) ? value : value + suffix);
    } catch (MissingResourceException e) {
      return returnValueIfNoFound ? value : "";
    }
  }
}
