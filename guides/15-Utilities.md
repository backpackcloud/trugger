# Utilities

Trugger has a couple of utilities used by itself and may be useful for you.

## Annotation Mock

You can mock an Annotation using the *AnnotationMock* class:

    import static org.atatec.trugger.util.mock.Mock.annotation;
    import static org.atatec.trugger.util.mock.Mock.mock;
    import javax.annotation.Resource;
    // ...
    Resource resource = new AnnotationMock<Resource>(){{
          map("name").to(annotation.name());
          map(false).to(annotation.shareable());
        }}.mock();
    String name = resource.name(); //returns "name"
    boolean shareable = resource.shareable(); //return false
    // returns "" because it is the default value
    String mappedName = resource.mappedName();
    // return javax.annotation.Resource class
    Class<? extends Annotation> type = resource.annotationType();


The DSL exposed allows a mock of an Annotation with defaults in a easy way:

    import static org.atatec.trugger.util.mock.Mock.annotation;
    import static org.atatec.trugger.util.mock.Mock.mock;

    Resource resource = mock(annotation(Resource.class));

## Class Iteration

An *Iterator* that uses the hierarchy of the target class. Useful in *foreach* loops:

    import static org.atatec.trugger.reflection.hierarchyOf;

    for (Class type : hierarchyOf(target)) {
      //do something with the class
    }

## Null Objects

A set of
