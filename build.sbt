name := "akkafutures"

resolvers += "Akka" at "http://akka.io/repository"

resolvers += "Guiceyfruit" at "http://guiceyfruit.googlecode.com/svn/repo/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor" % "2.0-SNAPSHOT",
  "com.typesafe.akka" % "akka-remote" % "2.0-SNAPSHOT"
)
