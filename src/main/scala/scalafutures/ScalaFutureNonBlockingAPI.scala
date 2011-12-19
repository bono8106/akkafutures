package scalafutures

import scala.actors.!
import scala.actors.Future
import scala.actors.InputChannel
import scala.actors.Actor
import scala.actors.Actor._

object ScalaFutureNonBlockingAPI extends App {

  /**
   * Note: while the E API is fully nonblocking here, like in E,
   * it must return Nothing, and therefore lacks the chaining
   * of when statements, which is possible in E.
   *
   * @author Nikolay
   *
   */
  abstract class EActor extends Actor {

    def receive: PartialFunction[Any, Unit]

    private lazy val behavior = receive

    final override def act() {
      eventloop(behavior)
    }

    final def when[T](future: Future[T])(block: T => Unit): Nothing = when(future.inputChannel)(block)

    final def when[T](inputChannel: InputChannel[T])(block: T => Unit): Nothing = {
      eventloop { // wait for the future in a non-blocking fashion
        case inputChannel ! response =>
          try {
            block(response.asInstanceOf[T])
          } finally {
            eventloop(behavior) // we got the future result, so go back to original eventloop
          }
        case other => behavior(other) // continue handling other messages while waiting on the future's result
      }
    }

    this.start()
  }

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

  val client = new EActor {
    def receive = {
      case serviceProvider: Actor =>
        log("Client enter " + self)
        val future = serviceProvider !! 42
        self ! "can interleave with nonblocking receive on future"
        log("Entering blockiing receive...")
        log("The future's input channel = " + future.inputChannel)
        log("My channel = " + self)
        when (future) { response =>
            log("Client got future result " + response)
            self ! response
            Thread.sleep(3000)
            log("Client future react done")
        }
        log("Client leave") // never reached
      case response: Int =>
        log("Client got back " + response)
      case message: String =>
        log("Message: " + message)
    }
  }

  client ! service

}