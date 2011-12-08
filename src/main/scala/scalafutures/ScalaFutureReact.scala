package scalafutures

import scala.actors.Actor
import scala.actors.Actor._

object ScalaFutureReact extends App {

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
    lazy val handler: PartialFunction[Any, Unit] = {
      case serviceProvider: Actor =>
        log("Client enter " + self)
        val future = serviceProvider !! 42;
        self mkBody {
          future.inputChannel react {
            case response: Int =>
              log("Client got future result " + response)
              self ! response
              Thread.sleep(1000)
              log("Client future react done")
          }
        } andThen eventloop(handler)
        log("Client leave") // never reached
      case response: Int =>
        log("Client got back " + response)
    }

    eventloop(handler)
  }

  client ! service
  client ! service

}