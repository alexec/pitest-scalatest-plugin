
name := "pitest-scalatest-plugin"

organization := "org.pitest"

version := "1.1.10-SNAPSHOT"

crossScalaVersions := Seq("2.11.8")

libraryDependencies += "org.pitest" % "pitest" % "1.1.10"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6"
