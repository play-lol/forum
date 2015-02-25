import PlayKeys._
name := """play-lol"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  json,
  filters,
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "com.typesafe.play" %% "play-slick" % "0.8.1" withJavadoc() withSources()
)

// libraryDependencies += "ws.securesocial" %% "securesocial" % "master-SNAPSHOT" withJavadoc() withSources()

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "org.webjars" % "bootstrap" % "3.3.2",
  "org.webjars" % "jquery" % "2.1.3",
  "org.webjars" % "requirejs" % "2.1.15"
)

resolvers += Resolver.sonatypeRepo("snapshots")

//scalacOptions := Seq("-encoding", "UTF-8", "-Xlint", "-deprecation", "-unchecked", "-feature")
//routesImport ++= Seq("scala.language.reflectiveCalls")
