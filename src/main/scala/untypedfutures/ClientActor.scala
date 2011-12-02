package untypedfutures

import akka.actor.Actor
import akka.actor.ActorRef

case class Go(other: ActorRef)

class ClientActor extends Actor {

    def receive = {
      case Go(other: ActorRef) => 
        log("Client enter")
        val future = other ? 10
        future onResult {
          case x =>
            log("Client got future result " + x)
            self ! x
            Thread.sleep(1000)
            log("Client future callback done.")
        }
        other ! 5
        log("Client leave")
      case other => log("Client got message " + other)
    }

}