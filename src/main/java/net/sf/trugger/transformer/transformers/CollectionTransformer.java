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
package net.sf.trugger.transformer.transformers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

import net.sf.trugger.annotation.AcceptArrays;
import net.sf.trugger.annotation.AcceptedTypes;
import net.sf.trugger.transformer.BidirectionalTransformer;
import net.sf.trugger.validation.validator.NotNull;

/**
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
@AcceptArrays
@AcceptedTypes(Object.class)
public class CollectionTransformer implements BidirectionalTransformer<Collection, Object> {

  private net.sf.trugger.transformer.transformers.Collection annotation;

  @Override
  public Object inverse(@NotNull Collection collection) {
    Object array = Array.newInstance(annotation.arrayType(), collection.size());
    int i = 0;
    for (Object object : collection) {
      Array.set(array, i++, object);
    }
    return array;
  }

  @Override
  public java.util.Collection transform(@NotNull Object object) {
    int length = Array.getLength(object);
    Collection collection = new ArrayList(length);
    for (int i = 0 ; i < length ; i++) {
      collection.add(Array.get(object, i));
    }
    return collection;
  }

}
