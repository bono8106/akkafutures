package untypedfutures.letitcrash

import akka.actor.Actor
import akka.actor.ActorRef
import untypedfutures.{namedThreadActorOf, log}

object LetItCrashTest extends App {

  case object Fail

  class ServiceActor extends Actor {

    var n = 0

    def receive = {
      case i: Int =>
        log("service " + n + " - " + i)
        n += 1
      case Fail =>
        Thread.sleep(1000) // let mailbox fill up
        throw new RuntimeException("Service actor failing.")
    }

  }

  class ClientActor extends Actor {

    def receive = {
      case s: ActorRef =>
        for (i <- 1 to 10) {
          s ! i
        }
        s ! Fail
        for (i <- 11 to 20) {
          s ! i
        }
    }

  }

  val service = namedThreadActorOf(new ServiceActor, "service")
  val client = namedThreadActorOf(new ClientActor, "client")

  client ! service

}