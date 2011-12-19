package tellorask

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.dispatch.Future

object SilentFailureWithAsk extends App {

  val actorSystem = ActorSystem()

  class Service extends Actor {
    def receive = {
      case x: Int => throw new Exception("ask failed")
    }
  }

  class Client extends Actor {
    def receive = {
      case service: ActorRef =>
        // ActorRef.ask failure
        implicit def timeout = actorSystem.settings.ActorTimeout
        service ? 42 onComplete { result => println("Ask completed with " + result) }

        // Future.apply failure
        implicit def dispatcher = actorSystem.dispatcher
        Future { throw new Exception("future failed") } onComplete { result => println("Future completed with " + result) }
    }
  }

  val service = actorSystem.actorOf(Props(new Service))
  val client = actorSystem.actorOf(Props(new Client))

  client ! service

}