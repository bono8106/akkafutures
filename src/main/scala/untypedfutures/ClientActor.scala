package untypedfutures

import akka.actor.Actor
import akka.actor.ActorRef

case class Go(other: ActorRef)

class ClientActor extends Actor {
    self.dispatcher = newThreadDispatcher("client")

    def receive = {
      case Go(other: ActorRef) => 
        log("In client actor")
        val future = other ? 10
        future onResult {
          case x => log("Got result " + x); self ! x
        }
        log("Out of client actor")
      case other => log("Client Actor got " + other)
    }

}