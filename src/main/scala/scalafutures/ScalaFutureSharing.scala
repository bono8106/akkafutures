package scalafutures

import scala.actors.Actor
import scala.actors.Actor._
import scala.actors.Future

object ScalaFutureSharing extends App {

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

  val otherUsingInputChannel = actor {
    eventloop {
      case future: Future[_] =>
        future.inputChannel react {
          case response: Int =>
            log("Other (inputChannel) got future's result " + response)
        }
      case msg: String =>
        log("Other (inputChannel) message " + msg)
    }
  }

  val otherUsingRespond = actor {
    eventloop {
      case future: Future[_] =>
        future respond {
          case response: Int =>
            log("Other (respond) got future's result " + response)
        } // never completes (uses react internally)
      case msg: String =>
        log("Other (respond) message " + msg)
    }
  }

  val client = actor {
    lazy val handler: PartialFunction[Any, Unit] = {
      case serviceProvider: Actor =>
        log("Client enter " + self)
        val future = serviceProvider !! 42
        otherUsingInputChannel ! future
        otherUsingRespond ! future
        Thread.sleep(20)
        otherUsingInputChannel ! "Hello"
        otherUsingRespond ! "Hello"
        self mkBody {
          self ! "cannot interleave with blocking receive on future"
          log("Entering blockiing receive...")
          log("The future's input channel = " + future.inputChannel)
          log("My channel = " + self)
          future.inputChannel react {
            case response: Int =>
              log("Client got future result " + response)
              self ! response
              Thread.sleep(3000)
              log("Client future react done")
          }
        } andThen eventloop(handler)
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