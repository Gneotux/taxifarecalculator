name := "taxiFareCalculator"

version := "1.0"

lazy val `taxifarecalculator` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.0"
)