import sbt._

object AkkaFuturesBuild extends Build {
  lazy val root = Project(id = "akkafutures", base = file("."))
}
