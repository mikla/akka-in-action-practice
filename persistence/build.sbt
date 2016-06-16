name := "akka-in-action-practice persistence"

version := "1.0"

scalaVersion := "2.11.7"

val akkaVersion = "2.3.12"
val sprayVersion = "1.3.3"

parallelExecution in Test := false // since we use a shared file-based journal
fork := true

libraryDependencies ++=
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-persistence-experimental" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion % "test",
    "org.scalatest" %% "scalatest" % "2.2.0" % "test"
  )


    