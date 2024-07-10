ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    name := "ScalaInterviewTechTasks"
  )

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.6.19",
  "com.typesafe.akka" %% "akka-http" % "10.2.10",
  "org.jsoup" % "jsoup" % "1.14.3",
  "com.typesafe.akka" %% "akka-stream" % "2.6.19",
  "de.heikoseeberger" %% "akka-http-circe" % "1.37.0",
  "io.circe" %% "circe-core" % "0.14.3",
  "io.circe" %% "circe-generic" % "0.14.3",
  "io.circe" %% "circe-parser" % "0.14.3"
)