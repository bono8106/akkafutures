package typedfutures

import akka.actor.TypedActor._

trait Client {
  def go(service: Service)
  def other(other: Any)
}

class ClientActor extends Client with Serializable {

    def go(service: Service ) {
        log("Client enter")
        val future = service.square(10)
        val myself = self[Client]
        future onSuccess {
          case x =>
            log("Client got future result " + x)
            myself.other(x)
            Thread.sleep(1000)
            log("Client future callback done.")
        }
        log("Client leave")
    }

    def other(other: Any) {
      log("Client got message " + other)
    }

}