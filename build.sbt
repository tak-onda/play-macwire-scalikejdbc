name := """play-macwire-scalikejdbc"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.6",
  "com.softwaremill.macwire" %% "macros" % "1.0.7",
  "com.softwaremill.macwire" %% "runtime" % "1.0.7"
)

routesGenerator := play.routes.compiler.InjectedRoutesGenerator
