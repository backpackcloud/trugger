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
package net.sf.trugger;

import net.sf.trugger.predicate.Predicable;

/**
 * A result that can be converted into a predicate that evaluates as
 * <code>true</code> all results returned by
 * {@link net.sf.trugger.PredicableResult#in(Object) it}.
 *
 * @author Marcelo Varella Barca Guimarães
 * @param <Value>
 *          the result object type
 * @param <Target>
 *          the target type
 * @since 1.2
 */
public interface PredicableResult<Value, Target> extends Predicable<Value>, Result<Value, Target> {

}
