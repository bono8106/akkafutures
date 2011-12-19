package scalafutures

import scala.actors.!
import scala.actors.Actor
import scala.actors.Actor._

object ScalaFutureNonBlocking extends App {

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
        val future = serviceProvider !! 42
        val futureChannel = future.inputChannel
        self ! "can interleave with nonblocking receive on future"
        log("Entering blockiing receive...")
        log("The future's input channel = " + future.inputChannel)
        log("My channel = " + self)
        eventloop {
          case futureChannel ! response =>
            log("Client got future result " + response)
            self ! response
            Thread.sleep(3000)
            log("Client future react done")
            eventloop(handler) // we got the future result, so go back to original eventloop
          case other => handler(other) // continue handling other messages while waiting on the future's result
        }
        log("Client leave") // never reached
      case response: Int =>
        log("Client got back " + response)
      case message: String =>
        log("Message: " + message)
    }

    eventloop(handler)
  }

  client ! service

}