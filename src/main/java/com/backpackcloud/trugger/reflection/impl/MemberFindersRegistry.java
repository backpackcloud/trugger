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

package com.backpackcloud.trugger.reflection.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/** @author Marcelo Guimaraes */
public interface MemberFindersRegistry {

  MemberFinder<Field> fieldFinder(String name);

  MembersFinder<Field> fieldsFinder();

  MemberFinder<Method> methodFinder(String name, Class... args);

  MembersFinder<Method> methodsFinder();

  MemberFinder<Constructor<?>> constructorFinder(Class... args);

  MembersFinder<Constructor<?>> constructorsFinder();

}
