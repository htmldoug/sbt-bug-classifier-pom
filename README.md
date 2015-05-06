Demonstrates a bug in sbt that produces a missing scope in the generated pom.xml

## Steps
1. `sbt makePom`
2. Open the generated pom.xml
3. Observe that the dependency entry for `"guava-tests" % "18.0" % "test" classifier "tests"` is missing a `<scope>test</scope>` element.

## Problems
The following library dependencies... 
```scala
libraryDependencies += "com.google.guava" % "guava-tests" % "18.0"
libraryDependencies += "com.google.guava" % "guava-tests" % "18.0" % "test" classifier "tests"
```
... produce a pom dependency with a missing `<scope>test</scope>`.

```xml
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava-tests</artifactId>
    <version>18.0</version>
    <classifier>tests</classifier>
</dependency>
```

## Expectations
The dependency with configuration "test" should produce a pom entry with `<scope>test</scope>`. 

## Notes

### Interaction issue?
On its own, the following produces the correct result.
```scala
libraryDependencies += "com.google.guava" % "guava-tests" % "18.0" % "test" classifier "tests"
```
```xml
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava-tests</artifactId>
    <version>18.0</version>
    <classifier>tests</classifier>
    <scope>test</scope>
</dependency>
```

It's only when you add a libraryDependency with the same group + artifactId + version in compile configuration *in addition* to the test configuration with the classifier, that the problem occurs. 

### pom generation vs dependencyClasspath

dependencyClasspath and test:dependencyClasspath appear okay. This may be isolated to the pom generation.

```
> export dependencyClasspath
/Users/doug/.sbt/boot/scala-2.10.4/lib/scala-library.jar:
/Users/doug/.ivy2/cache/com.google.guava/guava-tests/jars/guava-tests-18.0.jar:
/Users/doug/.ivy2/cache/com.google.code.findbugs/jsr305/jars/jsr305-1.3.9.jar

> export test:dependencyClasspath
/code/sbt-bug-classifier-pom/target/scala-2.10/classes:
/Users/doug/.sbt/boot/scala-2.10.4/lib/scala-library.jar:
/Users/doug/.ivy2/cache/com.google.guava/guava-tests/jars/guava-tests-18.0-tests.jar:
/Users/doug/.ivy2/cache/com.google.guava/guava-tests/jars/guava-tests-18.0.jar:
/Users/doug/.ivy2/cache/com.google.code.findbugs/jsr305/jars/jsr305-1.3.9.jar
```
