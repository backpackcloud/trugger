# Overview

Trugger is a framework that helps you write code that anyone can read. The idea is to provide a set of fluent interfaces to deal with operations related to reflection (such as creating proxies, reflecting members and scanning classes).

Trugger is intended to be a base for creating infrastructure code. While it is not an IoC container, for example, it could be a part of the core of an IoC container.

## What a hell does the name trugger mean?

When I was learning java, I choosed "Atatec" (*Ataxexe Technology*) to be the name of my fictitious company and then I sent a message to my friend Ives:

> Atatec

And the response was:

> trugger

When I asked him why he came up with this, he said:

> Isn't that the name of that special kick in Street Fighter?

The special kick mentioned is the *Tatsumaki Senpuu Kyaku* and Ken says something like "atatec trugger" (at least in Portuguese) when he does the kick. So the name Trugger became my first choice when I start to write this framework.

## How To Use

Just put the jar file on your **classpath** and you're done. No dependencies are required by Trugger at runtime.

## How To Build

Just make sure you have [Gradle][]. The markdown files are converted at build time using [pandoc][] so you should have it to build the distribution files.

[gradle]: <http://gradle.org>
[pandoc]: <http://johnmacfarlane.net/pandoc/README.html>

## How To Contribute

Just fork the project, do some stuff and send me a pull request. You can also fire an issue or tell your friends to use Trugger.

## About Maven Repository

Trugger currently is not in the Maven Central Repository, but a pom is generated and distributed in the download so you can import it on your local repository.

# Reflection

The Reflection module was the first one to show up in Trugger, it allows simple and much less verbose use of the Reflection API to reflect constructors, methods, fields and generic type declarations.

The base class of this module is `Reflection` and you will find a nice fluent interface there with the methods `reflect`, `invoke` and `handle`.

## Fields

### Single

Reflecting a field is done with the code `reflect().field(field name)`. You can also apply a filter by using the method `filter` and the target class (or object) by using the method `in`. If you need a recursive search through the entire target class hierarchy, just use the method `deep`.

Here is some examples (assuming a `static import`):

```java
Field value = reflect().field("value").in(String.class);

Field name = reflect().field("name").deep().in(MyClass.class);

Field id = reflect().field("id")
    .filter(field -> field.getType().equals(Long.class))
    .in(someInstance);
```

The `filter` method receive a `java.util.function.Predicate` to stay in touch with Java 8.

### Multiple

If you need to reflect a set of fields, use the `reflect().fields()`. The same features of a single reflect is available here.

~~~java
List<Field> stringFields = reflect().fields()
    .filter(field -> field.getType().equals(String.class))
    .deep()
    .in(MyClass.class);
~~~

### Predicates

There are some builtin predicates in the class
`FieldPredicates`.

~~~java
List<Field> stringFields = reflect().fields()
    .filter(type(String.class)) // a static import
    .deep()
    .in(MyClass.class);
~~~

### Handling Values

A field can have its value manipulated through the method `Reflection#handle`. It will create a handler to set and get the field's value without the verbosity of the Reflection API. To handle static fields, you can call the handler methods directly:

~~~java
String value = handle(field).get();
handle(field).set("new value");
~~~

For instance fields, just specify an instance using the method `in`:

~~~java
String value = handle(field).in(instance).get();
handle(field).in(instance).set("new value");
~~~

You can also use a a `FieldSelector`:

~~~java
// static import Reflection#field
String value = handle(field("name")).in(instance).value();
~~~

## Constructors

### Single

To reflect a constructor, use `reflect().constructor()` and specify the parameter types and optionally a filter. If the class has only one constructor, it can be reflected without supplying the parameter types.

~~~java
Constructor constructor = reflect()
  .constructor().withParameters(String.class).in(MyClass.class);

Constructor constructor = reflect().constructor()
  .withoutParameters().in(MyClass.class);

Constructor constructor = reflect().constructor()
  .filter(c -> c.isAnnotationPresent(SomeAnnotation.class))
  .withParameters(String.class).in(MyClass.class);

Constructor constructor = reflect().constructor().in(MyClass.class)
~~~

### Multiple

A set of constructors can be reflected by using `reflect().constructors()`. As in fields, the features in single reflection are present in multiple reflection.

~~~java
List<Constructor> constructors = reflect().constructors().in(MyClass.class);

List<Constructor> constructors = reflect().constructors()
  .filter(c -> c.getParameterCount() == 2)
  .in(MyClass.class);
~~~

Note that in multiple selection you cannot specify the parameter types directly in the fluent interface (for obvious reasons).

### Predicates

A few useful predicates are included in the class `ConstructorPredicates`.

~~~java
List<Constructor> constructors = reflect().constructors()
  .filter(annotated()) // a static import
  .in(MyClass.class);
~~~

### Invocation

To invoke a constructor you need an `Invoker`. The method `Reflection#invoke` returns a Invoker for a constructor. Specify the parameters and the constructor will be invoked.

~~~java
Constructor c = reflect().constructor()
  .withParameters(String.class).in(String.class)
String name = invoke(c).withArgs("Trugger");
~~~

## Methods

### Single

To reflect a single method, just pass it name to `Reflection#method`. Filtering is allowed and you can specify parameter types too.

~~~java
Method toString = reflect().method("toString").in(Object.class);

Method remove = reflect().method("remove")
  .withParameters(Object.class, Object.class)
  .in(Map.class);

Method someMethod = reflect().method("foo")
  .filter(method -> method.isAnnotationPresent(PostConstruct.class))
  .in(instance);
~~~

As in field reflection, you can do a deep search with `deep`.

~~~java
Method toString = reflect().method("toString").deep().in(MyClass.class);
~~~

### Multiple

A set of methods can be reflected by using `Reflection#methods`:

~~~java
List<Method> methods = reflect().methods().in(Object.class);
~~~

Deep search and filtering are also supported:

~~~java
List<Method> methods = reflect().methods()
  .filter(method -> method.isAnnotationPresent(PostConstruct.class)
  .deep()
  .in(MyClass.class);
~~~

### Predicates

A set of predicates to deal with methods is in `MethodPredicates`:

~~~java
List<Method> methods = reflect().methods()
  .filter(annotatedWith(PostConstruct.class)) // a static import
  .deep()
  .in(MyClass.class);
~~~

### Invocation

To invoke a method, use the Invoker returned by `Reflection#invoke`. Instance methods needs an instance provided using the method `in`:

~~~java
Method toString = reflect().method("toString").in(String.class);
invoke(toString).in("A string").withoutArgs();
~~~

Static methods don't need it:

~~~java
Method parseInt = reflect().method("parseInt")
  .withParameters(String.class)
  .in(Integer.class);
int number = invoke(parseInt).withArgs("10");
~~~

Note that you can also use a `MethodSelector`:

~~~java
invoke(method("toString")).in("A string").withoutArgs();
~~~

## Generic Type

Generic declarations in a class are present in the bytecode. Trugger can reflect them by using the method `genericType`. Suppose we have this interface:

~~~java
public interface Repository<T> {
  // ... some useful methods here
}
~~~

A generic base implementation can use that **T**:

~~~java
public class BaseRepository<T> {

  private final Class<T> type;

  protected BaseRepository() {
    this.type = reflect().genericType("T").in(this);
  }
}
~~~

The constructor was declared `protected` to warn that this will only work for subclasses (it is a Java limitation). A workaround to use this trick in a variable-like way is by declaring an anonymous class:

~~~java
Repository<MyType> repo = new BaseRepository<MyType>(){};
~~~

I think this is an ugly solution, but works.

# Class Scanning

Another cool feature Trugger has is the class scanning. Just give a package name and Trugger will scan it for finding classes. The class scanning feature starts at `ClassScan`.

The scanning starts in the method `ClassScan#scan`, which returns a `ClassScanner` that allows changing the ClassLoader and defining the package.

~~~java
List<Class> classes = scan().classes().in("my.package");
~~~

This will return every class in the package `my.package`. To do a deep scan and also return the classes in subpackages, use the `deep` method:

~~~java
List<Class> classes = scan().classes().deep().in("my.package");
~~~

You can also filter the classes with the method `filter`:

~~~java
List<Class> classes = scan().classes()
  .deep()
  .filter(c -> c.isAnnotatedWith(Entity.class))
  .in("my.package");
~~~

## Predicates

In `ClassPredicates` is a set of useful predicates to deal with classes.

~~~java
List<Class> classes = scan().classes()
  .deep()
  .filter(annotatedWith(Entity.class)) //static import
  .in("my.package");

List<Class> classes = scan().classes()
  .deep()
  .filter(subtypeOf(Repository.class)) //static import
  .in("my.package");
~~~

## Resource Finders for Protocols

The search is performed based on protocols. The basic protocols are `file` and `jar` and are supported by Trugger. More specific protocols can be handled by creating a `ResourceFinder` and registering it with the method `ClassScan#register`. A `ResourceFinder` is responsible for finding any resources in a given package and it must support a protocol. Trugger has a couple of finders registered to the following protocols:

- **jar** - for resources in a jar file
- **file** - for resources in the filesystem
- **vfs** - for resources in jar files deployed on a JBoss AS 7.x
- **vfszip** - for resources in jar files deployed on a JBoss AS 5.x and 6.x
- **vfsfile** - for resources in files deployed on a JBoss AS 5.x and 6.x

Any finder registered to an already supported protocol will override the
registered finder.

# Proxy Creation

A proxy object is created using a interception handler and optionally a fail handler. The DSL exposed starts at `Interception` with the method `intercept` and lets you define one or more interfaces to intercept. Additionally, you can set a target and its interfaces will be used. After the behaviour specification, use the method `proxy` to create the proxy instance.

~~~java
SomeInterface proxy = Interception.intercept(SomeInterface.class)
  .onCall(context -> logger.info("method intercepted: "
    + context.method())
  .proxy();

proxy.doSomething();
~~~

The interception logic happens in the handler passed through the method `onCall`. The handler receives a context, which contains all information about the intercepted method. A fail handler can also be set using the method `onFail`.

~~~java
SomeInterface proxy = Interception.intercept(SomeInterface.class)
  // sets a target to delegate the call using the context object
  .on(instance)
  // delegates the call to the target (this is the default behaviour)
  .onCall(context -> context.invoke())
  // handles any error occurred
  .onFail((context, throwable) -> handleTheFail(throwable))
  .proxy();

proxy.doSomething();
~~~

The fail handler has access to the context so you can delegate the method to the target again (if a timeout occurs, for example).

## The Interception Context

The interception context holds everything related to the method interception, included:

- The arguments passed
- The method intercepted
- The declared method intercepted in the target instance
- The proxy instance
- The target instance (may be null if not specified when creating the proxy)

The context can be used to delegate the method call to the target (using `invoke`) or to another instance (using `invokeOn`). The declared method intercepted can be retrieve by using `targetMethod`.

# Elements of an Object

## What is an Element?

An element is any value that an object holds. It may be accessible through a field, invoking a method (a getter or a setter) or even a specific way like the `Map#get` method.

A basic element in Trugger is a Property or a Field. Trugger tries to find a getter and a setter method for the element name and a field with the same name. This allows manipulate private fields and properties in the same way without bothering you with the way of handling the value.

## Obtaining an Element

An element is obtained using the method `element` in `Elements`. The same features of a field reflection is here with the addition of getting an element without specifying a name. A set of predicates are present in `ElementPredicates`.

~~~java
Element value = element("value").in(MyClass.class);

Element id = element()
  .filter(annotatedWith(Id.class)) // static import
  .in(MyClass.class);

List<Element> strings = elements()
  .filter(type(String.class) // static import
  .in(MyClass.class);
~~~

## Copying Elements

The elements of an object can be copied to another object, even if they are from different types. The DSL starts at the method `copy`:

~~~java
copy().from(object).to(anotherObject);
~~~

This will copy every element. To restrict the copy to non null values, use the `notNull` method:

~~~java
copy().from(object).notNull().to(anotherObject);
~~~

You can also apply a function to transform the values before assigning them to the target object (useful when copying values to a different type of object).

~~~java
copy().from(object)
  .applying(copy -> copy.value().toString())
  .to(anotherObject);
~~~

To filter the elements to copy, just give a selector to the `copy` method:

~~~java
copy(elements().filter(annotatedWith(MyAnnotation.class)))
  .from(object)
  .to(anotherObject);
~~~

Or filter the copy directly using `filter`

~~~java
copy(elements().filter(annotatedWith(MyAnnotation.class)))
  .from(object)
  .filter(copy -> copy.dest().isAnnotationPresent(MyAnnotation.class))
  .to(anotherObject);
~~~

## Nested Elements

Nested elements are supported using a **"."** to separate the elements:

~~~java
Element element = element("address.street").in(Customer.class);

value = element.in(customer).get();
~~~

You can use any level of nesting:

~~~java
Element element = element("customer.address.street").in(Response.class);

value = element.in(response).get();
~~~

## Custom Elements

Some classes have a custom definition of elements. A `Map` has their keys as elements, an `Array` has their indexes as elements an so on. Elements are found by an element finder (a class that implements `Finder<Element>`) and you can write a custom element finder and register it using the registry available through the method `Elements#registry`.

Trugger has custom element finders for a set of java core classes:

- `Map`: keys are used as the elements
- `ResourceBundle`: keys are used as the elements
- `Properties`: keys are used as the elements
- `ResultSet`: the column names are used as the elements
- `Annotation`: the methods as used as elements
- `List`: indexes are used as the elements (and also two special names, *first* and *last*)
- Arrays: indexes are used as the elements (and also two special names, *first* and *last*)

It is important to have clear that since this elements are instance specific, the elements should be queried by passing an instance instead of a class for the method `in` or an empty list will be returned. For a single elements, you may pass a class or an instance but using an instance is better because you can call the handling methods directly.

You can also use this custom element finders to copy elements easily:

~~~java
// this will copy every element from the result set to the instance
copy().from(resultSet).to(myEntity);
~~~

# Utilities

## Annotation Mock

Annotations are interfaces and mocking it should be as easy as mocking an interface. The problem is the default values that can be omitted. Trugger has an utility module to help mocking and annotation by using the interception module.

To create a mock, you should start by creating an anonymous class that extends `mock.AnnotationMock` and maps the elements in a block code inside the class using the methods `map` and `to`.

~~~java
Resource resource = new AnnotationMock<Resource>(){{
    map("name").to(annotation.name());
    map(false).to(annotation.shareable());
}}.createMock();

//returns "name"
String name = resource.name();

//return false
boolean shareable = resource.shareable();

//returns "" because it is the default value
String mappedName = resource.mappedName();

// returns javax.annotation.Resource class
Class<? extends Annotation> type = resource.annotationType();
~~~

If you don't like the anonymous class style, you can still use the classic
style.

~~~java
AnnotationMock<Resource> mock = new AnnotationMock<>(Resource.class);

mock.map("name").to(annotation.name());
mock.map(false).to(annotation.shareable());

Resource resource = mock.createMock();
~~~

## Context Factories

If you need a lightweight component to invoke a constructor with a predicate based logic to resolve the parameter values, you can use the `ContextFactory`.

A `ContextFactory` is a factory that maps a predicate that evaluates parameters to an object or supplier. After creating a `ContextFactory`, you can manipulate the context through the `#context` method and create an object with the `create` method. A set of predicates can be found in `ParameterPredicates` class.

~~~java
ContextFactory factory = new ContextFactory();
factory.context()
  //static imports
  .use(myImplementation)
    .when(type(MyInterface.class))
  .use(someObject)
    .when(named("component"))
  .use(parameter ->
      resolve(parameter.getAnnotation(MyAnnotation.class)))
    .when(annotatedWith(MyAnnotation.class))
  .use(() -> availableWorker())
    .when(type(MyWorker.class));
~~~

The above factory will:

1. use `myImplementation` for any parameter of the type `MyInterface`
2. use `someObject` for any parameter named *"component"*
3. use the return of `resolve` with the annotation `MyAnnotation` for any parameter annotated with `MyAnnotation`
4. use the return of `availableWorker` to any parameter of type `MyWorker`

These steps will be done with every public constructor of a type, if a constructor has one parameter that cannot be resolved to an object, then the next constructor will be used and if there is no more constructors to use, an exception is thrown.

## Component Factories

Component factories allows creating components defined by annotations. Suppose
you have:

~~~java
public @interface ComponentClass {

  Class<? extends Component> value();

}

@ComponentClass(MyComponentImplementation.class)
public @interface MyComponent {

  String name();

}

//inside a class

@MyComponent(name = "myName")
private String aField;
~~~

The annotation in `aField` can be used to create an instance of `MyComponentImplementation`. The context used to create any components are:

1. Every property of the annotation with their specific types (in that case, the property `name` with the value *"myName"* to a parameter named `name` and of type `String`)
2. The annotation itself with its type (in that case, the `MyComponent` annotation to the type `MyComponent`)

Since the annotation is used as the context, you can have a constructor in the component implementation that receives the annotation instead of its properties. This is useful if you don't want to compile your code with `-parameters` parameter.

This behaviour is completely replaceable by using the method `configureContextWith`. To add behaviour to the default one, compose the `ComponentFactory#defaults` with your behaviour:

~~~java
factory.configureContextWith(
  defaults().andThen(
    (context, annotation) -> yourConfigurations
  )
);
~~~

To instantiate a component, just use a code like this one:

~~~java
ComponentFactory<ComponentClass, Component> factory =
  new ComponentFactory(ComponentClass.class);

// get the annotation from the field

Component component = factory.create(annotation);
~~~

Alternatively, you can get a list of components by passing an `AnnotatedElement`
to the method `#createAll`:

~~~java
Element = Elements.element("aField").in(myObject);
List<Component> components = factory.createAll(element);
~~~

Or creating a single one by passing an `AnnotatedElement` to the method `#create`:

~~~java
Element = Elements.element("aField").in(myObject);
Component component = factory.create(element);
~~~

# Validation

The validation module is not a replacement for the *Bean Validation*. It combines the utility factories and Element module to provide a basic and simple engine to do only validations (message production is out of scope). This is a good component for backend validations or even to integrate validation in proprietary or old infrastructure codes.

## Basic Validation

To validate an object, you can simply use the Validation class and the provided DSL:

~~~java
ValidationResult result = Validation.engine().validate(object);
~~~

The result will give all information needed to integrate the validation to almost all frameworks and architectures. (You can access values and use reflection and other DSLs using the invalid elements.)

## Validation using filters

You can pass a selector for restrict the elements to validate:

~~~java
ValidationResult result = Validation.engine()
  .filter(ofType(String.class)))
  .validate(object);
~~~

The given filter will affect any nested validations that relies on a supplied ValidationEngine.

## Creating Validators

### Basic Validators

The validation constraints are defined using two components: an Annotation for defining the constraint and a Validator to implement it.

**Example:** A basic validator for null objects.

~~~java
@Retention(RetentionPolicy.RUNTIME)
@ValidatorClass(NotNullValidator.class)
public @interface NotNull { }

public class NotNullValidator implements Validator {

  public boolean isValid(Object object) {
    return object != null;
  }

}
~~~

You can now annotate a field or a getter method:

~~~java
public class Person {

  @NotNull
  private String name;

  //...

}
~~~

### Type Validators

You can create validators for a specific type or a set of types using the validator generic type or the `MultiTypeValidator` component.

**Example:** a regex validator.

~~~java
@Retention(RetentionPolicy.RUNTIME)
@ValidatorClass(PatternValidator.class)
public @interface Pattern {

  String value();

}

public class PatternValidator implements Validator<CharSequence> {

  private final String pattern;
  private final int flags;

  // the constraint will be automatically injected in the constructor
  public PatternValidator(Pattern constraint) {
    this(constraint.value(), constraint.flags());
  }

  public boolean isValid(CharSequence value) {
    if(value != null) {
      return true;
    }
    java.util.regex.Pattern pattern =
      java.util.regex.Pattern.compile(annotation.value());
    return pattern.matcher(value).matches();
  }
}
~~~

In this example, everything that is a CharSequence can be validated with `@Pattern`. If you need to change the behaviour of the constraint validation without creating one constraint for each type, just use the `MultiTypeValidator` (as a superclass or using composition) to map each type to a validator.

~~~java
public class NotEmptyValidator extends MultiTypeValidator {

  @Override
  protected void initialize() {
    map(Collection.class).to(this::isCollectionValid);
    mapArray().to(array -> array.length == 0);
    map(Map.class).to(map -> !map.isEmpty());
  }

  private isCollectionValid(Collection collection) {
    return !collection.isEmpty();
  }

}
~~~

### Validation of arguments

Take a look at the first instruction in the validation method of `PatternValidator`. That check can be a pain if you doesn't like boilerplate code. To avoid that you can use validations in parameter declared in `isValid` method.

~~~java
  public boolean isValid(@NotNull CharSequence value) {
    java.util.regex.Pattern pattern =
      java.util.regex.Pattern.compile(annotation.value());
    return pattern.matcher(value).matches();
  }
~~~

Much more elegant!

### Dependency Injection

#### Constraint values

The constraint can be injected in the constructor by passing the entire constraint (like in the PatternValidator example above) or its properties using their names (in case you compile the code using "-parameters") or their types (much less accurate, but works for constraints that has properties with different types).

The `PatternValidator` above may have a constructor `public PatternValidator(String pattern, int flags)` to get rid of the constraint dependency.

#### Validation Engine

If your validator requires a ValidationEngine, you can define a parameter in the constructor and an engine will be injected here. The difference between this and calling `Validation.engine()` directly is that the injected engine will have the same filter applied before the validation (using the method `ValidationEngine#filter`).

#### Target Object

If your validator requires the object being validated, just declare a parameter annotated with `TargetObject` and the target will be injected there.

#### Element

If your validator requires the `Element` being validated, just declare a parameter of type `Element` and it will be injected there.

#### Target Elements

This is one of the most useful injections. To explain this injection, suppose you have two dates and one must be after the other.

~~~java
public class Ticket {

  private Date leaving;

  private Date arrival;

  //...

}
~~~

Validating the `arrival` field will require access to the `leaving` field. This can be done by using element references in the annotation. Let's look the `@After` constraint:

~~~java
@Retention(RetentionPolicy.RUNTIME)
@ValidatorClass(AfterValidator.class)
public @interface After {

  @TargetElement("reference")
  String value();

}

public class AfterValidator implements Validator<Date> {

  private final Date referenceValue;

  public AfterValidator(@NotNull Date reference) {
    this.reference = reference;
  }

  public boolean isValid(@NotNull Date value) {
    return value.after(referenceValue);
  }

}
~~~

Notice that the references can be validated in constructor. If the reference is invalid the validation will not be processed.

The class will now looks like this:

~~~java
public class Ticket {

  @NotNull
  private Date leaving;

  @NotNull
  @After("leaving")
  private Date arrival;

  //...

}
~~~

The value that will be passed to the constructor will be the element with the same name as the `value` property in the constraint `After`. The dependency resolution will use the property name (in the example, `value`) and a parameter name (in the example, `reference`) to help finding the value for each constructor parameter (remember to use "-parameters" to compile the code).

### Merging Invalid Elements

Sometimes is useful to do a complete validation over the element value and incorporate their invalid elements in the main result. To do such a validation, just add a constructor parameter of type `ValidationEngine`:

~~~java
public class ValidValidator implements Validator {
  private final ValidationEngine engine;

  public ValidValidator(ValidationEngine engine) {
    this.engine = engine;
  }

  public boolean isValid(@NotNull Object value) {
    return !engine.validate(value).isInvalid();
  }

}
~~~

This solves the validation problem. But the invalid elements are lost in the validation result inside the `ValidValidator`. To incorporate this elements, just annotate the constraint that maps to the `ValidValidator` with `@MergeElements`:

~~~java
@MergeElements
@Retention(RetentionPolicy.RUNTIME)
@ValidatorClass(ValidValidator.class)
public @interface Valid {
}
~~~

This will make the ValidationEngine passed to the validator to incorporate the invalid elements into the invalid result using nested elements without changing any code in the validator.

### Shared Validators

If your validator is thread safe, you can mark it to be shared across every validation by using the annotation `@Shared` on it. Keep in mind that validators that uses constraint properties or dependencies related to the current validation (like a validation engine) will not behave well if they are shared.

## Domain Validations

Suppose you have some properties that requires more than one validation. You can group then into a single constraint (a domain constraint) using the DomainValidator as the @ValidatorClass.

**Example:**

~~~java
@NotNull
@After("leaving")
@Retention(RetentionPolicy.RUNTIME)
@ValidatorClass(DomainValidator.class)
public @interface ArrivalDate {}

public class Ticket {

  @NotNull
  private Date leaving;

  @ArrivalDate
  private Date arrival;

  //...

}
~~~

By using the `DomainValidator` you can group validations to create a domain constraint and reuse code. Just keep in mind that merging elements is a main constraint, so you must annotate the domain constraint with `@MergeElements` to merge the invalid elements (if you group a `@Valid` constraint, for example).

# Extending

## How To Implement the Fluent Interfaces

The fluent interfaces are always defined through java interfaces and may be customized by your own implementation. Trugger uses a `ServiceLoader` to load a factory that knows the implementations to instantiate, so you can override the implementation of any fluent interface by defining a file in your **META-INF/services** directory with the factory implementation.

The factory interfaces that can be customized are listed bellow:

- `ElementFactory`: used for reflecting elements
- `ReflectionFactory`: used for reflection in general
- `ClassScannerFactory`: used for class scanning
- `InterceptorFactory`: used for method interception
- `ValidationFactory`: used for validation
