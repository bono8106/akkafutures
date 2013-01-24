package tellorask

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import scala.concurrent.Future
import akka.pattern.ask
import akka.util.Timeout

object SilentFailureWithAsk extends App {

  val actorSystem = ActorSystem()
  implicit def executionContext = actorSystem.dispatcher

  class Service extends Actor {
    def receive = {
      case x: Int => throw new Exception("ask failed")
    }
  }

  class Client extends Actor {
    def receive = {
      case service: ActorRef =>
        // ActorRef.ask failure
        implicit def timeout = Timeout(3000L)
        service ? 42 onComplete { result => println("Ask completed with " + result) }

        // Future.apply failure
        Future { throw new Exception("future failed") } onComplete { result => println("Future completed with " + result) }
    }
  }

  val service = actorSystem.actorOf(Props(new Service))
  val client = actorSystem.actorOf(Props(new Client))

  client ! service

}