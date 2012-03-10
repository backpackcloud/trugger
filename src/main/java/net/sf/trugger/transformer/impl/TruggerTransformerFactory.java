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
package net.sf.trugger.transformer.impl;

import net.sf.trugger.CreateException;
import net.sf.trugger.annotation.TargetElement;
import net.sf.trugger.annotation.processors.TargetElementResolver;
import net.sf.trugger.bind.Bind;
import net.sf.trugger.bind.Binder;
import net.sf.trugger.factory.AnnotationBasedFactory;
import net.sf.trugger.factory.AnnotationFactoryContext;
import net.sf.trugger.reflection.ReflectionPredicates;
import net.sf.trugger.transformer.BidirectionalTransformer;
import net.sf.trugger.transformer.TransformerClass;
import net.sf.trugger.transformer.TransformerFactory;
import net.sf.trugger.transformer.TransformerInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerTransformerFactory implements TransformerFactory {

  private AnnotationBasedFactory<TransformerClass, BidirectionalTransformer> factory =
      new AnnotationBasedFactory<TransformerClass, BidirectionalTransformer>() {

        protected BidirectionalTransformer defaultReturn(AnnotatedElement key) {
          return new BidirectionalTransformer() {

            @Override
            public Object inverse(Object object) {
              return object;
            }

            @Override
            public Object transform(Object object) {
              return object;
            }

          };
        }

        @Override
        protected BidirectionalTransformer instantiate(AnnotatedElement key, Class<? extends BidirectionalTransformer> classToCreate)
            throws Throwable {
          BidirectionalTransformer formatter;
          //don't try to subclass final classes
          if (ReflectionPredicates.FINAL_CLASS.evaluate(classToCreate)) {
            /*
             * The proxy for interface is not useful because the binds will not work.
             * So, the validator itself is returned.
             */
            formatter = super.instantiate(key, classToCreate);
          } else {
            formatter = new TransformerInterceptor().createProxy().implementing(BidirectionalTransformer.class).extending(classToCreate);
          }
          return formatter;
        }
      };

  @Override
  public boolean canCreate(AnnotationFactoryContext key) {
    return factory.canCreate(key.annotatedElement());
  }

  @Override
  public BidirectionalTransformer create(AnnotationFactoryContext key) throws CreateException {
    Binder binder = Bind.newBinder();
    Annotation annotation = key.annotation();
    BidirectionalTransformer transformer;
    if (annotation != null) {
      transformer = factory.create(annotation.annotationType());
      binder.bind(annotation).toElement().ofType(annotation.annotationType());
    } else {
      transformer = factory.create(key.annotatedElement());
    }
    if (key.target() != null || key.element() != null) {
      binder.use(new TargetElementResolver(key)).toElements().annotatedWith(TargetElement.class);
    }
    binder.applyBinds(transformer);
    return transformer;
  }

}
