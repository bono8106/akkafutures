package untypedfutures

import akka.actor.Actor

object RemoteFutureTest extends App {

  val service = Actor.actorOf(new ServiceActor).start

  Actor.remote.start("0.0.0.0", 9999)
  Actor.remote.register("service", service)

  val remoteService = Actor.remote.actorFor("service", "localhost", 9999)

  val client = Actor.actorOf(new ClientActor).start

  client ! Go(remoteService)

}