name := "akka-in-action-practice remoting"

version := "1.0"

scalaVersion := "2.11.7"

val akkaVersion = "2.3.12"
val sprayVersion = "1.3.3"

libraryDependencies ++=
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "org.scalatest" %% "scalatest" % "2.2.0" % "test"
  )


    