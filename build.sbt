name := "sbt-bug-classifier-pom"

organization := "sbt.example"

libraryDependencies += "com.google.guava" % "guava-tests" % "18.0"

libraryDependencies += "com.google.guava" % "guava-tests" % "18.0" % "test" classifier "tests"

publishMavenStyle := true
