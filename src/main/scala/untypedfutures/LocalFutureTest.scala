package untypedfutures

import akka.actor.Actor
import akka.actor.ActorRef
import akka.dispatch.Dispatchers

object LocalFutureTest extends App {

  val service = Actor.actorOf(new ServiceActor).start
  val client = Actor.actorOf(new ClientActor).start

  client ! Go(service)

}
