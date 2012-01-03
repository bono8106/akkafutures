name := "akkafutures"

addCompilerPlugin("org.scala-tools.sxr" %% "sxr" % "0.2.8-SNAPSHOT")

scalacOptions <+= scalaSource in Compile map { "-P:sxr:base-directory:" + _.getAbsolutePath }

resolvers := Seq(
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
  "Akka" at "http://akka.io/repository/",
  "Akka-snapshots" at "http://akka.io/snapshots/",
  "Guiceyfruit" at "http://guiceyfruit.googlecode.com/svn/repo/releases/"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor" % "2.0-SNAPSHOT",
  "com.typesafe.akka" % "akka-remote" % "2.0-SNAPSHOT"
)

