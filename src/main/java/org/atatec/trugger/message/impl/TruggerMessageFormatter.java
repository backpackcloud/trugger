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
package org.atatec.trugger.message.impl;

import org.atatec.trugger.annotation.Reference;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.Elements;
import org.atatec.trugger.message.MessageFormatter;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * The default MessageFormatter. This formatter allows the use of context and
 * {@link org.atatec.trugger.annotation.Reference} value and element info in the message.
 * <p>
 * To use a context value in the message, just put the desired name of the
 * element between <i>${</i> and <i>}</i>. To use an info, use the following
 * patterns:
 * <ul>
 * <li>name - the name of the element
 * <li>type - the type of the element
 * <li>value - the value of the element
 * </ul>
 * <p>
 * The context elements can also be used.
 * <p>
 * There are some expression that can be used for advanced format:
 * <ul>
 * <li><i>#{ }</i> - searches for the content in the <i>ResourceBundle</i>.
 * <li><i>@{ , ...}</i> - the content will be formatted using a
 * {@link java.util.Formatter}, the first parameter is the format and the subsequent (if
 * applies), the arguments.
 * </ul>
 * Note that the expressions only works in elements that exists in the context.
 * Example: if you define the expression <i>#{element.name}</i> and the context
 * element is <i>otherElement</i>, the expression will not work.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public final class TruggerMessageFormatter implements MessageFormatter {

  private static final Pattern SPLIT_PATTERN = Pattern.compile("\\s*,\\s*");

  public String format(String message, ResourceBundle bundle, Element targetElement, Object contextObject, Object target) {
    Map<String, Object> map = new LinkedHashMap<String, Object>();
    String elementName = targetElement.name();
    Object value = targetElement.in(target).value();
    StringBuilder buff = new StringBuilder(message);
    
    for (Element contextElement : Elements.elements().in(contextObject)) {
      Object elementValue = contextElement.in(contextObject).value();
      if (contextElement.isAnnotationPresent(Reference.class)) {
        String contextElementName = contextElement.name();
        Element referenceElement = Elements.element(String.valueOf(elementValue)).in(target);
        String referenceName = referenceElement.name();
        Object referenceValue = referenceElement.in(target).value();
        
        mapContextElementValue(map, bundle, '{' + contextElementName + ".name}", referenceName);
        mapContextElementValue(map, bundle, '{' + contextElementName + ".type}", referenceElement.type().getName());
        mapContextElementValue(map, bundle, '{' + contextElementName + ".value}", referenceValue);
      }
      mapContextElementValue(map, bundle, '{' + contextElement.name() + '}', elementValue);
    }
    mapElementValue(map, bundle, "{name}", elementName);
    mapElementValue(map, bundle, "{type}", targetElement.type().getName());
    mapElementValue(map, bundle, "{value}", value);
    format(map, buff, bundle.getLocale());
    Set<Entry<String, Object>> expressions = map.entrySet();
    for (Entry<String, Object> entry : expressions) {
      replace(buff, entry.getKey(), String.valueOf(entry.getValue()));
    }
    return buff.toString();
  }
  
  private void mapContextElementValue(Map<String, Object> map, ResourceBundle bundle, String expression, Object value) {
    map.put('$' + expression, value);
    mapBundleValue(map, bundle, expression, value);
  }
  
  private void mapElementValue(Map<String, Object> map, ResourceBundle bundle, String expression, Object value) {
    mapBundleValue(map, bundle, expression, value);
    map.put(expression, value);
  }
  
  private void mapBundleValue(Map<String, Object> map, ResourceBundle bundle, String expression, Object value) {
    map.put('#' + expression, getString(bundle, String.valueOf(value)));
  }
  
  private void format(Map<String, Object> map, StringBuilder buff, Locale locale) {
    int pos = 0;
    while ((pos = buff.indexOf("@{", pos)) != -1) {
      int index = indexOfClose(buff, '{', '}', pos);
      if (index > -1) {
        String[] strings = SPLIT_PATTERN.split(buff.substring(pos + 2, index));
        String pattern = strings[0];
        Object[] args = new Object[strings.length - 1];
        System.arraycopy(strings, 1, args, 0, args.length);
        
        for (int i = 0 ; i < args.length ; i++) {
          Object object = args[i];
          if (map.containsKey(object)) {
            args[i] = map.get(object);
          }
        }
        
        String formatted = String.format(locale, pattern, args);
        buff.replace(pos, index + 1, formatted);
      } else {
        break;
      }
    }
  }
  
  /**
   * Gets a string from the given bundle using a key, preventing a
   * {@link java.util.MissingResourceException} by returning the given key.
   * 
   * @param bundle
   *          the bundle for getting the string.
   * @param name
   *          the name of the key
   * @return the string or the given name if a MissingResourceException is
   *         raised.
   */
  private static String getString(ResourceBundle bundle, String name) {
    try {
      return name != null ? bundle.getString(name) : null;
    } catch (MissingResourceException e) {
      return name;
    }
  }
  
  /**
   * Replaces all the patterns occurrences in the StringBuilder with the
   * specified value.
   * 
   * @param buff
   *          the StringBuilder
   * @param pattern
   *          the patterns to search
   * @param value
   *          the value to replace
   */
  private static void replace(StringBuilder buff, String pattern, String value) {
    for (int i ; (i = buff.indexOf(pattern)) != -1 ;) {
      buff.replace(i, i + pattern.length(), value);
    }
  }
  
  /**
   * Calculates the index of the char thats closes the sentence started by the
   * open char.
   * 
   * @param sequence
   *          the sequence of chars for searching.
   * @param open
   *          the char that opens the sentence.
   * @param close
   *          the char that closes the sentence.
   * @param offset
   *          the offset for considering
   * @return the index of the char thats closes the sentence.
   */
  private static int indexOfClose(CharSequence sequence, char open, char close, int offset) {
    int pos = offset;
    int opened = 0;
    //stops only if the end of the buffer is reached
    while (pos < sequence.length()) {
      char c = sequence.charAt(pos);
      if (c == close) {
        //every opened bracket must be closed
        if (--opened == 0) {
          break;
        }
      } else if (c == open) {
        opened++;
      }
      pos++;
    }
    return pos == sequence.length() ? -1 : pos;
  }
  
}
