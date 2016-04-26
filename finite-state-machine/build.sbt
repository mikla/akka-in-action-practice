name := "akka-in-action-practice finite-state machine"

version := "1.0"

scalaVersion := "2.11.7"

val akkaVersion = "2.4.4"

libraryDependencies ++=
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-agent" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "org.scalatest" %% "scalatest" % "2.2.0" % "test",
    "com.typesafe.akka" %% "akka-remote" % akkaVersion,
    "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion % "test"
)


    