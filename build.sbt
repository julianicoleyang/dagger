ThisBuild / organization := "com.julia"
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / version := "0.1.0-SNAPSHOT"

mainClass in Compile := Some("com.julia.dagger.DagRunner")

lazy val root = (project in file("."))
  .settings(
    name := "dag-runner",
    libraryDependencies ++= Seq(
      "org.json4s" %% "json4s-native" % "4.0.0",
      "org.mockito" % "mockito-all" % "1.10.19" % Test,
      "org.scalactic" %% "scalactic" % "3.2.9",
      "org.scalatest" %% "scalatest" % "3.2.9" % Test
    )
  )
