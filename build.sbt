name := """play-macwire-scalikejdbc"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.softwaremill.macwire" %% "macros" % "1.0.7",
  "com.softwaremill.macwire" %% "runtime" % "1.0.7",
  "mysql" % "mysql-connector-java" % "5.1.6",
  "org.scalikejdbc" %% "scalikejdbc" % "2.2.8",
  "org.scalikejdbc" %% "scalikejdbc-config" % "2.2.8",
  "com.zaxxer" % "HikariCP" % "1.3+"
)

routesGenerator := play.routes.compiler.InjectedRoutesGenerator
