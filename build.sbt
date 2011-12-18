name := "akkafutures"

addCompilerPlugin("org.scala-tools.sxr" %% "sxr" % "0.2.8-SNAPSHOT")

scalacOptions <+= scalaSource in Compile map { "-P:sxr:base-directory:" + _.getAbsolutePath }

resolvers += "Akka" at "http://akka.io/repository"

resolvers += "Guiceyfruit" at "http://guiceyfruit.googlecode.com/svn/repo/releases/"

libraryDependencies ++= Seq(
  "se.scalablesolutions.akka" % "akka-actor" % "1.3-RC4",
  "se.scalablesolutions.akka" % "akka-typed-actor" % "1.3-RC4",
  "se.scalablesolutions.akka" % "akka-remote" % "1.3-RC4"
)
