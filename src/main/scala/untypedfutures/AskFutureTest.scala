package untypedfutures

import akka.actor.Actor
import akka.actor.ActorRef
import akka.dispatch.Dispatchers

object AskFutureTest extends App {

  def newThreadDispatcher(name: String) = Dispatchers.
    newExecutorBasedEventDrivenDispatcher(name).setCorePoolSize(1).setMaxPoolSize(1).build

  def log(msg: String) {
    println("[" + Thread.currentThread.getName + "] " + msg)
  }

  case class Go(other: ActorRef)

  class AskFutureService extends Actor {
    self.dispatcher = newThreadDispatcher("service")

    def receive = {
      case x: Int =>
        log("In actor 2")
        val y = x*x
        self.channel tryTell y
        log("Out of actor 2")
    }

  }

  class AskFutureTest extends Actor {
    self.dispatcher = newThreadDispatcher("tester")

    def receive = {
      case Go(other: ActorRef) => 
        log("In actor 1")
        val future = other ? 10
        future onResult {
          case x => log("Got result " + x)
        }
        log("Out of actor 1")
      case other => log("Actor 1 got " + other)
    }

  }
  
  val a1 = Actor.actorOf(new AskFutureService).start
  
  val a2 = Actor.actorOf(new AskFutureTest).start
  
  a2 ! Go(a1)

}
