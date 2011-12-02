package untypedfutures

import akka.actor.Actor
import akka.actor.Props

object LocalFutureTest extends App {

  val service = namedThreadActorOf(new ServiceActor, "service")
  val client = namedThreadActorOf(new ClientActor, "client")

  client ! Go(service)

}
