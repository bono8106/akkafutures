package untypedfutures

import akka.actor.Actor

object RemoteFutureTest extends App {

  def startService {
    val service = Actor.actorOf(new ServiceActor).start

    Actor.remote.start("0.0.0.0", 9999)
    Actor.remote.register("service", service)
  }

  def startClient {
    val remoteService = Actor.remote.actorFor("service", "localhost", 9999)
    val client = Actor.actorOf(new ClientActor).start

    client ! Go(remoteService)
  }

  args match {
    case Array("--client") => startClient
    case Array("--server") => startService
    case _ => startService; startClient
  }

}