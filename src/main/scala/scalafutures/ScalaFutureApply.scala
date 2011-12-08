package scalafutures

import scala.actors.Actor
import scala.actors.Actor._

object ScalaFutureApply extends App {

  def log(msg: Any) {
    println("[" + Thread.currentThread.getName + "] " +  "{" + Actor.self + "} " + msg)
  }

  val service = actor {
    eventloop {
      case x: Int =>
        log("Service enter with sender " + sender)
        sender ! (x * x)
        Thread.sleep(1000)
        log("Service leave")
    }
  }

  val client = actor {
    eventloop {
      case serviceProvider: Actor =>
        log("Client enter " + self)
        val future = serviceProvider !! 42
        future() match {
          case response: Int =>
            log("Client got future result " + response)
            self ! response
            Thread.sleep(1000)
            log("Client future apply done")
        }
        log("Client leave")
      case response: Int =>
        log("Client got back " + response)
    }
  }

  client ! service

}