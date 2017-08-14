
lazy val commonSettings = Seq(
  name := "akka-in-action-practice clustering",
  version := "1.0",
  scalaVersion := "2.12.3"
)

val akkaVersion = "2.5.4"

val dependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,

  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.0.1" % Test,
  "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion % Test
)

lazy val app = (project in file("."))
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= dependencies)
  .settings(assemblyJarName in assembly := "words-node.jar")
