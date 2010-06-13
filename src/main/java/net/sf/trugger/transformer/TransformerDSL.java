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
package net.sf.trugger.transformer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.trugger.interception.InterceptionContext;
import net.sf.trugger.interception.InvocationTrackerInterceptor;
import net.sf.trugger.predicate.Criteria;
import net.sf.trugger.predicate.PredicateDSL;
import net.sf.trugger.reflection.Reflection;

/**
 * This class represents a DSL to build a {@link Transformer}.
 * <p>
 * This DSL provides a way of defining targets for each transformer and also
 * criterions.
 * <p>
 * Examples:
 *
 * <pre>
 * new TransformerDSL&lt;MyClass&gt;{{
 *   use(mytransformer).on(obj).when(obj.someMethod()).equal(desiredResult);
 * }}
 * </pre>
 * This example illustrates the use of {@link #obj} instance. This instance
 * should be used for defining targets and criterions.
 * <p>
 * In the first use, the whole instance passed to {@link #transform(Object)}
 * method will be passed to the transformer mapped to the criteria (in that
 * case, if <code>obj.someMethod()</code> equals <code>desiredResult</code>, the
 * <code>obj</code> will be passed to <code>mytransformer</code>).
 * <p>
 * Any other method can be used as a target, example:
 *
 * <pre>
 * new TransformerDSL&lt;MyClass&gt;{{
 *   use(mytransformer).on(obj.getName());
 * }}
 * </pre>
 * Now, the method <code>getName</code> invoked on the instance passed to
 * {@link #transform(Object)} will be passed to <code>mytransformer</code>, no
 * matter the conditions because no one is specified.
 * <p>
 * You can define many conditions, but only one generic. Example:
 *
 * <pre>
 * new TransformerDSL&lt;MyClass&gt;{{
 *    use(mytransformer).on(obj).when(obj.someMethod()).equal(desiredResult);
 *    use(simpletransformer).on(obj.getName()).when(obj.size()).lessThan(15);
 *    use(othertransformer).on(obj); //uses this transformer if the other conditions fails
 * }}
 * </pre>
 *
 * @author Marcelo Varella Barca Guimarães
 * @param <E>
 *          The object type
 * @since 2.6
 */
public class TransformerDSL<E> implements Transformer<Object, E> {

  /**
   * The proxy object for calling the methods while creating the DSL.
   */
  protected E obj;
  private final Class<E> clazz;

  private final InvocationTrackerInterceptor interceptor;

  private final Map<PredicateDSL<E>, MapValue> map = new LinkedHashMap<PredicateDSL<E>, MapValue>();

  private class MapValue {

    Transformer transformer;
    List<InterceptionContext> contexts;
    PredicateDSL<E> predicate;

    MapValue(Transformer transformer, PredicateDSL<E> predicate) {
      this.transformer = transformer;
      this.predicate = predicate;
    }

  }

  protected TransformerDSL() {
    clazz = Reflection.reflect().genericType("E").in(this);
    interceptor = new InvocationTrackerInterceptor();
  }

  /**
   * Sets the transformer to use.
   *
   * @param transformer
   *          the transformer to use for a condition.
   * @return a component for defining the target.
   */
  public TransformerTargetSelector<E> use(Transformer transformer) {
    PredicateDSL<E> predicate = new PredicateDSL<E>(clazz);
    obj = (E) interceptor.createProxy().over(clazz);
    MapValue mapValue = new MapValue(transformer, predicate);
    map.put(predicate, mapValue);
    interceptor.track();
    return new TransformerTarget<E>(mapValue);
  }

  public Object transform(E object) {
    Set<Entry<PredicateDSL<E>, MapValue>> entrySet = map.entrySet();
    MapValue mapValue;
    Object resolved;
    for (Entry<PredicateDSL<E>, MapValue> entry : entrySet) {
      if (entry.getKey().evaluate(object)) {
        mapValue = entry.getValue();
        resolved = InvocationTrackerInterceptor.resolve(object, mapValue.contexts);
        return mapValue.transformer.transform(resolved);
      }
    }
    return object;
  }

  private class TransformerTarget<E> implements TransformerTargetSelector<E> {

    private MapValue mapValue;

    private TransformerTarget(MapValue mapValue) {
      this.mapValue = mapValue;
    }

    public TransformerCriteria<E> on(Object value) {
      mapValue.contexts = interceptor.trackedContexts();
      obj = mapValue.predicate.obj();
      return new TransformerCriteriaImpl(mapValue.predicate);
    }

  }

  private class TransformerCriteriaImpl<E> implements TransformerCriteria<E> {

    private PredicateDSL<E> predicate;

    private TransformerCriteriaImpl(PredicateDSL<E> predicate) {
      this.predicate = predicate;
    }

    public <V> Criteria<E, V> when(V value) {
      return predicate.expect(value);
    }

    public <V> Criteria<E,V> whenNot(V value) {
      return predicate.dontExpect(value);
    }

    public void when(Boolean value) {
      predicate.expect(value.booleanValue());
    }

    public void when(boolean value) {
      predicate.expect(value);
    }

    public void whenNot(boolean value) {
      predicate.dontExpect(value);
    }

    public void whenNot(Boolean value) {
      whenNot(value.booleanValue());
    }

  }

}
