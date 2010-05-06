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
package net.sf.trugger.test.property;

import net.sf.trugger.test.Flag;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class TestObject {
  
  @Flag
  private final String name;
  
  int age;
  
  boolean active;
  
  final double price;
  
  final String readable;
  
  String writable;
  
  Object allAccess;
  
  long fieldProp;
  
  long otherFieldProp = 10;
  
  public TestObject(String name, int age, double price, boolean active, String readable) {
    this.name = name;
    this.age = age;
    this.active = active;
    this.price = price;
    this.readable = readable;
  }
  
  public void setName(char[] name) {

  }
  
  public void setActive(double active) {

  }
  
  public Object getAllAccess() {
    return allAccess;
  }
  
  public void setAllAccess(Object allAccess) {
    this.allAccess = allAccess;
  }
  
  public void setWritable(String writable) {
    this.writable = writable;
  }
  
  public String getReadable() {
    return readable;
  }
  
  public void setActive(boolean active) {
    this.active = active;
  }
  
  public boolean isActive() {
    return active;
  }
  
  public String getName() {
    return name;
  }
  
  public double getPrice() {
    return price;
  }
  
  public long getFieldProp() {
    return fieldProp;
  }
  
  public void setFieldProp(long fieldProp) {
    this.fieldProp = fieldProp;
  }
  
  public long getOtherFieldProp() {
    return otherFieldProp;
  }
  
}
