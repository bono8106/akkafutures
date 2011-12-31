package tellorask
import com.typesafe.config.ConfigFactory

object ActorsWithNonblockingFutures extends App {

  import akka.actor.Actor
  import akka.actor.ActorRef
  import akka.actor.ActorSystem
  import akka.actor.Props

  lazy val actorSystem = ActorSystem("test", ConfigFactory.parseString("""
      dispatchers {
        client.type = "PinnedDispatcher"
        service.type = "PinnedDispatcher"
      }
      """))

  implicit val actorTimeout = actorSystem.settings.ActorTimeout

  def namedThreadActorOf(f: => Actor, name: String) =
    actorSystem.actorOf(Props(creator = f _, dispatcher = "dispatchers." + name), name = name)

  def log(msg: String) {
    println("[" + Thread.currentThread.getName + "] " + msg)
  }

  class ServiceActor extends Actor {
    def receive = { case x: Int =>
        log("Service processing " + x)
        sender ! (x * x)
    }
  }

  class ClientActor extends Actor {
    def receive = {
      case serviceProvider: ActorRef => serviceProvider ? 42 onSuccess {
          case response: Int => log("Client got back " + response)
        }
    }
  }

  val service = namedThreadActorOf(new ServiceActor, "service")
  val client = namedThreadActorOf(new ClientActor, "client")

  client ! service

}