package untypedfutures

import akka.actor.Actor

object LocalFutureTest extends App {

  val service = namedThreadActorOf(new ServiceActor, "service")
  val client = namedThreadActorOf(new ClientActor, "client")

  client ! Go(service)

}
