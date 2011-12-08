package scalafutures

import scala.actors.Actor
import scala.actors.Actor._

object ScalaIdiomaticActors extends App {

  def log(msg: Any) {
    println("[" + Thread.currentThread.getName + "] " +  "{" + Actor.self + "} " + msg)
  }

  val service = actor {
    eventloop {
      case x: Int =>
        log("Service processing " + x)
        sender ! (x * x)
        Thread.sleep(1000)
        log("Service leave")
    }
  }

  val client = actor {
    eventloop {
      case serviceProvider: Actor =>
        log("Client enter " + self)
        serviceProvider ! 42
        log("Client leave")
      case response: Int =>
        log("Client got back " + response)
    }
  }

  client ! service

}