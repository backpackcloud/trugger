# Reflection

Trugger comes with a DSL for using reflection in a easy way. You can reflect fields,
methods and constructors with predefined or custom selectors (using a *Predicate*).
Generic types and bridge methods are also supported.

## Exposed DSL

The dsl starts with *Reflection* class. Besides of the utility methods, there are some
methods that returns the exposed interfaces. The basic flow is described below.

1) Choose to reflect a single or multiple elements
1) Optionally define a set of selectors
1) Define the target object

