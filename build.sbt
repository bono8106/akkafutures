name := "akkafutures"

resolvers += "Akka" at "http://akka.io/repository"

resolvers += "Guiceyfruit" at "http://guiceyfruit.googlecode.com/svn/repo/releases/"

libraryDependencies ++= Seq(
  "se.scalablesolutions.akka" % "akka-actor" % "1.3-RC1",
  "se.scalablesolutions.akka" % "akka-typed-actor" % "1.3-RC1",
  "se.scalablesolutions.akka" % "akka-remote" % "1.3-RC1"
)
