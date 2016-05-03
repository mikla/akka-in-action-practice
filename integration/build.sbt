name := "akka-in-action-practice integration"

version := "1.0"

scalaVersion := "2.11.7"

val akkaVersion = "2.4.4"

libraryDependencies ++=
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-camel" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    "org.scala-lang.modules" %% "scala-xml" % "1.0.3",
    "org.apache.commons" % "commons-io" % "1.3.2",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "org.scalatest" %% "scalatest" % "2.2.0" % "test"
  )


    