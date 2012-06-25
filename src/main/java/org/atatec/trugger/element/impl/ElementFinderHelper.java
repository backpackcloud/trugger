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
package org.atatec.trugger.element.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.atatec.trugger.element.Element;
import org.atatec.trugger.iteration.Iteration;
import org.atatec.trugger.transformer.Transformer;

/**
 * A helper class for element finders.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class ElementFinderHelper {

	public static Set<Element> computeResult(Object target, Collection<Element> elements) {
		if (target instanceof Class<?>) {
			return new HashSet<Element>(elements);
		}
		Set<Element> result = new HashSet<Element>(elements.size());
		Transformer<Element, Element> transformer = new SpecificElementTransformer(target);
		Iteration.copyTo(result).transformingWith(transformer).all().from(elements);
		return result;
	}

}
