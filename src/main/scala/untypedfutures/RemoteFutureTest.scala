package untypedfutures

import akka.actor.Actor

object RemoteFutureTest extends App {

  def startService {
    val service = actorSystem.actorOf(new ServiceActor)

//    TODO actorSystem.remote.start("0.0.0.0", 9999)
//    Actor.remote.register("service", service)
  }

  def startClient {
//    TODO val remoteService = Actor.remote.actorFor("service", "localhost", 9999)
//    val client = Actor.actorOf(new ClientActor).start
//
//    client ! Go(remoteService)
  }

  args match {
    case Array("--client") => startClient
    case Array("--server") => startService
    case _ => startService; startClient
  }

}