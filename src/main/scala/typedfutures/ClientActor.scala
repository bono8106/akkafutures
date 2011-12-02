package typedfutures

import akka.actor.TypedActor

trait Client {
  def go(service: Service)
  def other(other: Any)
}

class ClientActor extends TypedActor with Client with Serializable {

    def go(service: Service ) {
        log("Client enter")
        val future = service.square(10)
        val myself = context.getSelfAs [Client]
        future onResult {
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