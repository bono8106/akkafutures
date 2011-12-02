package untypedfutures

import akka.actor.Actor
import akka.actor.ActorRef

case class Go(other: ActorRef)

class ClientActor extends Actor {
    self.dispatcher = newThreadDispatcher("client")

    def receive = {
      case Go(other: ActorRef) => 
        log("Client enter")
        val future = other ? 10
        future onResult {
          case x => log("Client got future result " + x); self ! x
        }
        log("Client leave")
      case other => log("Client got message " + other)
    }

}