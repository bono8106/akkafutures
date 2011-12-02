package untypedfutures

import akka.actor.Actor

object LocalFutureTest extends App {

  val service = Actor.actorOf(new ServiceActor).start
  val client = Actor.actorOf(new ClientActor).start

  client ! Go(service)

}
