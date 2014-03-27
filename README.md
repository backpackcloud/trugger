# Overview

Trugger is a framework that helps you write code that anyone can read. The
idea is to provide a set of fluent interfaces to deal with operations related
to reflection (such as creating proxies, reflecting members and scanning
classes)

# How To Use

Just put the jar file on your **classpath** and you're done. Trugger does not
require any dependency at runtime.

# Modules

## Reflection

### Fields

### Constructors

### Methods

## Class Scanning

### Basic Scanning

### Defining a Protocol Handler

## Proxy Creation

## Elements of an Object

### What is an Element?

### Creating a Custom Element Finder

## Utilities

### Annotation Mock

### Class Hierarchy Iteration

# Extending

## How To Implement the Fluent Interfaces

The fluent interfaces are always defined through java interfaces and may be
customized by your own implementation. Trugger uses a `ServiceLoader` to load
a factory that knows the implementations to instantiate,
so you can override the implementation of any fluent interface by defining a
file in your **META-INF/services** directory with the factory implementation.

The factory interfaces that can be customized are listed bellow:

- org.atatec.trugger.element.ElementFactory
- org.atatec.trugger.property.PropertyFactory
- org.atatec.trugger.reflection.ReflectionFactory
- org.atatec.trugger.scan.ClassScannerFactory
- org.atatec.trugger.interception.InterceptorFactory
