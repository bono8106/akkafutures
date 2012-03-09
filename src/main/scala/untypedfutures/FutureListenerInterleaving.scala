package untypedfutures

import akka.actor.Actor
import akka.pattern.ask

object FutureListenerInterleaving extends App {
  
  val future = namedThreadActorOf(new Actor {
    def receive = {
      case x: Int =>
        Thread.sleep(20)
        println("sender = " + sender)
        sender ! x * x
    }
  }, "testactor") ? 42

  future onComplete { result =>
    println("handler A enter for " + result)
    Thread.sleep(200)
    println("handler A leave")
  }

  Thread.sleep(100)

  future onComplete { result =>
    println("handler B enter for " + result)
    Thread.sleep(200)
    println("handler B leave")
  }

}