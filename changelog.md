# Changelog

## Version 5.1

This release marks the reborn of **Validation** module in a different way. The
validation module now is a simple solution for manually validating objects
(useful in backend processing). It is not a implementation for the *JSR-303* nor
a competitor.

### New Modules

  - Validation

### New Features

  - `InterceptionHandler` for validating method arguments using the Validation
    module.

### Minor Changes

  - Context Factories now sorts the constructors descending using their
  parameter counts

## Version 5.0

This is a huge update. I'm going to focus on keep this project simple and
easy to maintain (since I don't have money to put a great effort on it). Lots
of code changed and lots of modules gone to `/dev/null`.

This release is also the first one to have a README with a basic guide.

### Major Changes

  - Java 8 support
    - Removed **Predicate**, **Iteration** and **Date** modules
    - Removed the tons of selector classes (lambdas are now a good solution) and
      everything are now centred in `filter` methods

  - Renamed `Invoker#handlingExceptionsWith` to `onError`
  - Unique searches no longer throws exceptions if two or more results are found
  - Reformulated the Interception module, now the DSL implementation can be
    changed
  - Immutable classes
  - Element copies only applies functions to non null values (to prevent *NPEs*)
  - No more single selections for field and method without specifying a name
  - No more exception handlers for invocations (method and constructor)
  - Removed `Reflection#newInstanceOf` method
  - Changed the return of selections to `List`
  - Renamed get and set methods in `ValueHandler`
  - Reformulated Class Scan module
  - `recursively` changed to `deep` and belongs now to `DeepSelector`
  - ContextFactory utility class
  - ComponentFactory utility class (a grown up version of the removed
    `AnnotationBasedFactory`)

### Minor Changes
  - Removed non used classes
  - Renamed some methods and classes to improve code readability

## Version 4.3

### Major Changes

  - Using `ServiceLoader` for loading implementations

## Version 4.2

Three modules removed since I don't have time/money to maintain them.

### Modules removed

  - Validation
  - Bind
  - Annotation (only Domain Annotation was kept)

### Minor Improvements

  - `Reflection#newInstanceOf` uses `Utils#resolveType`

## Version 4.1.1

### Bug Fixes

  - Fixed a bug with getter method finder on overrided methods.

## Version 4.1

### Major Changes

  - Added the possibility to reflect only public members of a class or the
    declared ones.
  - `ImplementationLoader#getInstance` renamed to `instance`
  - Removed `AccessSelector`
  - Refactor on `ReflectionPredicates`
  - Getter and Setter specs changed to include more cases
  - Iteration DSL improved
    - Finding operation separated in Find class
  - Class scan DSL improved
  - Interceptor class simplified and improved DSL to create a proxy
  - Reflection DSL improved
  - Predicates improved
  - Predicable interface removed
  - Changed scope of EasyMock to test
    - Mocks for Element and ElementFinder are now in test packages
    - Annotation mock still in the binaries, but is implemented using Interceptor
      class instead of EasyMock
  - Bind DSL improved
  - Validation DSL improved
    - Removed pluggable binders and factories (use the composite ones to override
      the default ValidationFactory

### Other Changes

  - Using Gradle as the build system (good bye, Maven)

## Version 4.0.1

- Fixed a bug when using a non-named field as selector for binding.

## Version 4.0

### New Modules

  - Exception Handling

### Major Changes

  - Packages renamed from **net.sf** to **org.atatec** (back to the origin ^_^)
  - pom groupId moved to **org.atatec.trugger**
  - Reflection Invokers and Handlers does not throw a NPE if a null object is
    passed
  - Class hierarchy helpers removed. Use `ClassIterator` and `Reflection#hierarchyOf`
  - Support for JBoss Virtual Filesystem 3.1 (allows class scanning in JBoss AS 7.x)
  - Improved API for registering a ResourceFinder.
  - Method `RegistryEntry#registry` renamed to `RegistryEntry#value`
  - Method `Predicates#newComposition` removed. Use `Predicates#is` instead.
  - ElementFinder for arrays.

### Minor Changes

  - Using SoftReferences to cache annotation and object elements.

## Version 3.1

### Major Changes

  - Changed method `PredicateSelector#thatMatches` to `that`
  - Transformers to all wrapper classes
  - Simplified methods like `elementsMatching` and `allElements` in iteration
   module.

## Version 3.0
### Major Changes
  - Removed **cglib** and **commons collections** dependency
  - Removed PredicateDSL and TransformerDSL
  - Performance improvements in validation
  - Interceptor used only for interfaces

## Version 2.8

### Major Changes

  - Reformulated Reflection DSL
  - Code improvements
  - Support for **JBoss Virtual Filesystem** (allows class scanning in JBoss
    AS 5.x and 6.x)

### Bug fixes:

  - ElementMockBuilder now supports call for getAnnotation method.

## Version 2.7

### New Modules:

  - Domain Annotations

### Major changes:

  - Reformulated DSL Criteria.
  - Refactor in Interception module
  - Non-named element and field selector for bind operations.
  - AnnotationBased factory now searchs in the entire annotation levels.
  - Renamed method `Bind#newBind` to `newBinder`.
  - More operations in Reflector.
  - Renamed `BindableElement#getTarget` method to `target`.
  - Removed generic type "?" from `ReflectionPredicates` and `ClassScanner`.

### Bug fixes:

  - Binder can now bind null values.

## Version 2.6

### Major changes:

  - Validation Binder for using Seam Components inside of a validator.
  - Generic type reflection without specifying the generic parameter name.
  - Additions to PredicateDSL.
  - Renamed the bind(Resolver) method to use(Resolver).
  - Merged Field elements and Property elements, so, if you want a property
    behaviour use the Properties class instead of Elements.
  - New DSL for transform operations.

### Bug fixes:

  - Element type check on element copies.

## Version 2.5

### Major changes:

  - New Element selection without specifying a name.
  - Created a package for holding annotations for general use.
  - Reverted method `genericTypeFor` to `genericType` (the shortest name is
    better - **IMHO**).
  - New invocation tracker interceptor.
  - New DSL for predicates.
  - Scan for one single class.
  - New method newInstanceOf in Reflection class.

## Version 2.4

### New modules:

  - DSL for iterations
    - Refactoring in Collection module to Iteration module. Now the iterations
      are made using an iterator rather than a collection.

### Major changes:

  - New ValidatorBinder component for customize binds.
  - Exceptions for validation module.
  - `@Valid` can now use context and propagates the root context to nested validators.
  - `@ValidationContext` for bind the context into a Validator.
  - Handler for multiple Element objects.
  - Removed the Trugger class (since the project grows, it became very large).

### Bug fixes:

  - Non-null return if the element is not found in the annotation and the target
    is not a class.

## Version 2.3.1

### Major changes:

  - Added a method to remove a registry entry (included in a minor release since
    its a useful method).

## Version 2.3

### New modules:

  - DSL for class scan.

### Major changes:

  - Implementations are now configured through Registry interface.
  - Utility class for creating Factories.
  - New selection for non-named fields or methods.
  - Changed method `Reflector#bridgedMethod` to `bridgedMethodFor`.
  - Changed method `Reflector#genericType` to `genericTypeFor`.
  - New search operation for CollectionHandler.
  - Some validators for use in Brazil.
  - Renamed `ValidationStrategy#breakOnFalse` method to
    `breakOnFirstInvalidObject`.
  - Moved the ClassLoader option to ProxyCreator.

### Minor changes:

  - Source encode changed to UTF-8.

## Version 2.2.1

### Minor changes:

  - Maven support.

## Version 2.2

### Major changes:

  - `ValidatorFactory` now receives a context for doing the binds without the
    `ValidationEngine`.
  - New validators.

### Bug fixes:

  - Valid annotation doesn't validate the mapped context.
  - NumberFormatException if `min` or `max` of Range is not defined.

## Version 2.1

### New modules:

  - DSL for creating proxies.
  - DSL for Validation.

### Major changes:

  - Requirements:
    - cglib (and its dependencies).
    - hibernate-validator (for compile and adapter use).
  - Mock for annotations.
  - New methods in AnnotationElementSelector.
  - Removed the `nonSynthetic` selection (used by fields and methods selectors).
  - New `assignableTo` selection in `TypedElementSelector`.
  - Predicates for strict and assignable types.
  - New interfaces added to `MethodSelector` and `ConstructorSelector`.
  - Selection for return type on methods renamed to `returning`.
  - Changed selection `SetterMethodSelector#ofType` to `forType`.
  - Improved ElementMockBuilder class.
    - Removed notAnnotatedWith method because the class is using now the nice
      mock instead of the normal mock.
    - The annotations added will be returned by getDeclaredAnnotations and
      getAnnotations.

### Bug fixes:

  - Fixed a bug in TruggerElementsSelector that returns a null predicate if no
    selection is made.

## Version 2.0

### New modules:

  - DSL for date operations.

### Major changes:

  - Java 6
  - Easy mock dependency (only for mock package).
  - Renamed method `getPredicate` to `predicate` in PredicateBuilder class.
  - Removed the prefix `all` from `Reflector` and `Elements` methods.
  - Corrected misspelling of method `isEmpty` *(epic fail)*.
  - Interface `BaseElementSelector` renamed to `ElementSpecifier`.
  - New `FieldSpecifier` interface.
  - Changed to `in` the name of the method `on` in `MethodInvoker` interface.
  - Removed the bind to properties.
  - Mocks for helping tests.
  - Removed the Alias annotation.
  - Refactored `ImplementationLoader` class.
  - Changed `getType` to `type`, `getDeclaringClass` to `declaringClass` and
    `getName` to `name` in `Element` class.
  - All references to Property (interface, selector, etc.) are now referenced as
    Element.
  - `AnnotationProperty` migrated to **Element** module.
  - Renamed the `ElementCopy` methods `fromElement` and `toElement` for
    `sourceElement` and `destinationElement`.
  - Swapped the src and dest in Element copy.
  - Renamed the methods in Properties and Elements class.
  - Specific Elements.
  - Removed the nested properties, nested elements should be used now.
  - Added another way to define custom finders (via "define" method on Elements).
  - Added custom element finders for:
    - ResultSet
    - ResourceBundle
    - Properties
    - Map
  - Added the Finder interface.
  - Removed the possibility of specifying elements from fields or properties.
  - New exception hierarchy.
      - Removed `PropertyManipulationException` and added `HandlingException`.
    - Renamed `UnreadablePropertyException` to `UnreadableElementException` and
    `UnwritablePropertyException` to `UnwritableElementException`

### Minor changes:

  - Method toString implemented in predicates for better debugging.
  - New ofType method in ElementPredicates.
  - Changed the return type of ElementPredicates.assignableTo to CompositePredicate.

## Version 1.2

### Major changes:

  - Removed the listeners in the ImplementationLoader for prevent anonymous
    changes.
  - HierarchySelector replaced by RecursionSelector for better abstraction.
  - Merged the property factories (finder, selector and copy) into a new one
    with the "property" alias.
  - Changed the "composite-predicate" alias to "predicate".
  - Renamed the CompositePredicateFactory to PredicateFactory.
  - Changed the name of the method "on" (FieldHandler) to "in".
  - Changed the name of PropertyHandler to Properties.

### New features and improvements:

  - Improved the cache for Object properties (ObjectPropertyFinder).
  - New reflection of getter and setter methods for a field object.
  - New element module for more flexibility.
  - The collection operations now returns the number of affected elements.
  - New count operation for collections.
  - New Predicable interface for converting objects into predicates.
  - Removed the ClassUtils class, its methods are now in the new Utils class.
  - Removed the methods to return the handled objects (for Field, Method and Constructor).
  - Some improvements in ImplementationLoader class.
  - New bind module.
  - Added the `nonFinal` selection to the MemberSelector interface.

### Bug Fixes:

  - Fixed a bug in the CLASS predicate (it allows annotations).

## Version 1.1

### New features and improvements:

  - Added the AnnotatedElementSelector to the FieldSelector.
  - Unnecessary generic types removed.
  - Added a package for the selectors.
  - Changed the property selectors.
  - Replaced the HierarchyResult with the HierarchySelector
  - Changed the named member reflection predicate to take only one argument.
  - Refactoring in the properties implementation.
  - Refactoring in the reflection implementation.
  - More selector interfaces:
    - PredicateSelector
    - AccessSelector
    - GetterMethodSelector (used by Reflector)
    - SetterMethodSelector (used by Reflector)

## Version 1.0.1

### New features and improvements:

  - Improved the object property resolution if a class uses more than one setter
    to a property.
  - Added tests to the reflection predicates.

### Bug fixes:

  - Fixed a bug in the not assignable class predicate.
