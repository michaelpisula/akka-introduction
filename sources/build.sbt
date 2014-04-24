name := """akka-workshop"""

version := "1.0"

scalaVersion := "2.10.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.0",
  "com.typesafe.akka" %% "akka-persistence-experimental" % "2.3.0",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.0",
  "com.typesafe.akka" %% "akka-camel" % "2.3.0",
  "com.typesafe.akka" %% "akka-cluster" % "2.3.0",
  "com.typesafe.akka" %% "akka-contrib" % "2.3.0",
  "org.apache.camel" % "camel-jetty" % "2.11.1",
  "org.apache.camel" % "camel-websocket" % "2.11.1",
  "org.scalatest" %% "scalatest" % "1.9.1" % "test",
  "com.google.guava" % "guava" % "15.0",
  "junit" % "junit" % "4.11" % "test",
  "com.novocode" % "junit-interface" % "0.10" % "test"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
