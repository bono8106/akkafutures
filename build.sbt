name := "akkafutures"

scalaVersion := "2.10.0"

addCompilerPlugin("org.scala-sbt.sxr" %% "sxr" % "0.3.0-SNAPSHOT")

scalacOptions <+= scalaSource in Compile map { "-P:sxr:base-directory:" + _.getAbsolutePath }

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor_2.10" % "2.1.0" withSources,
  "com.typesafe.akka" % "akka-remote_2.10" % "2.1.0" withSources
)

EclipseKeys.withSource := true
