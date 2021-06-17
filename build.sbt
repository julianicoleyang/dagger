ThisBuild / organization := "com.julia"
ThisBuild / scalaVersion := "2.13.5"
ThisBuild / version := "0.2.0-SNAPSHOT"
ThisBuild / organizationName := "julia"

mainClass in Compile := Some("com.julia.dagger.DagRunner")

lazy val root = (project in file("."))
  .settings(
    name := "dag-runner",
    libraryDependencies ++= Seq(
      "org.json4s" %% "json4s-native" % "4.0.0",
      "org.scalactic" % "scalactic_native0.4_2.13" % "3.2.9",
      "org.scalatest" % "scalatest_native0.4_2.13" % "3.2.9" % Test
    )
  )
