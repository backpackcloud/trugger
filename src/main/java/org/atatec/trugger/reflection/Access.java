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
package org.atatec.trugger.reflection;

import java.lang.reflect.Member;

import org.atatec.trugger.predicate.CompositePredicate;

/**
 * Represents the possible access of an element.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public enum Access {
  
  /**
   * Represents the public access.
   */
  PUBLIC {
    
    @Override
    public CompositePredicate<Member> memberPredicate() {
      return ReflectionPredicates.PUBLIC;
    }
    
    @Override
    public CompositePredicate<Class> classPredicate() {
      return ReflectionPredicates.PUBLIC_CLASS;
    }
  },
  /**
   * Represents the default access.
   */
  DEFAULT {
    
    @Override
    public CompositePredicate<Member> memberPredicate() {
      return ReflectionPredicates.DEFAULT;
    }
    
    @Override
    public CompositePredicate<Class> classPredicate() {
      return ReflectionPredicates.DEFAULT_CLASS;
    }
    
  },
  /**
   * Represents the protected access.
   */
  PROTECTED {
    
    @Override
    public CompositePredicate<Member> memberPredicate() {
      return ReflectionPredicates.PROTECTED;
    }
    
    @Override
    public CompositePredicate<Class> classPredicate() {
      return ReflectionPredicates.PROTECTED_CLASS;
    }
  },
  /**
   * Represents the private access.
   */
  PRIVATE {
    
    @Override
    public CompositePredicate<Member> memberPredicate() {
      return ReflectionPredicates.PRIVATE;
    }
    
    @Override
    public CompositePredicate<Class> classPredicate() {
      return ReflectionPredicates.PRIVATE_CLASS;
    }
  },
  /**
   * Represents an access as visible as the protected access.
   */
  LIKE_PROTECTED {
    
    @Override
    public CompositePredicate<Class> classPredicate() {
      return PUBLIC.classPredicate().or(PROTECTED.classPredicate());
    }
    
    @Override
    public CompositePredicate<Member> memberPredicate() {
      return PUBLIC.memberPredicate().or(PROTECTED.memberPredicate());
    }
    
  },
  /**
   * Represents an access as visible as the default access.
   */
  LIKE_DEFAULT {
    
    @Override
    public CompositePredicate<Class> classPredicate() {
      return PRIVATE.classPredicate().negate();
    }
    
    @Override
    public CompositePredicate<Member> memberPredicate() {
      return PRIVATE.memberPredicate().negate();
    }
    
  };
  
  /**
   * @return a predicate that returns <code>true</code> if the evaluated
   *         {@link Member} has this access.
   */
  public abstract CompositePredicate<Member> memberPredicate();
  
  /**
   * @return a predicate that returns <code>true</code> if the evaluated
   *         {@link Class} has this access.
   */
  public abstract CompositePredicate<Class> classPredicate();
}
