package untypedfutures

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import com.typesafe.config.ConfigFactory

object RemoteFutureTest extends App {

  def startService {
    val serviceConfig = ConfigFactory.parseString("""
      akka.actor.provider = "akka.remote.RemoteActorRefProvider"

      # Enable untrusted mode for full security of server managed actors, allows
      # untrusted clients to connect.
      akka.remote.untrusted-mode = on
      akka.remote.netty {
        # The hostname or ip to bind the remoting to,
        # InetAddress.getLocalHost.getHostAddress is used if empty
        hostname = "localhost"

        # The default remote server port clients should connect to.
        # Default is 2552 (AKKA)
        port = 2552
      }
    """)
    val serviceSystem = ActorSystem("serviceSystem", ConfigFactory.load(serviceConfig))

    val service = serviceSystem.actorOf(Props(new ServiceActor), "service")
    log(service.path.toString)
  }

  def startClient {
    val clientConfig = ConfigFactory.parseString("""
      akka.actor.provider = "akka.remote.RemoteActorRefProvider"

      # Enable untrusted mode for full security of server managed actors, allows
      # untrusted clients to connect.
      akka.remote.untrusted-mode = on
      akka.remote.netty {
        # The hostname or ip to bind the remoting to,
        # InetAddress.getLocalHost.getHostAddress is used if empty
        hostname = ""

        # The default remote server port clients should connect to.
        # Default is 2552 (AKKA)
        port = 2553
      }
    """)
    val clientSystem = ActorSystem("clientSystem", ConfigFactory.load(clientConfig))

    val remoteService = clientSystem.actorFor("akka://serviceSystem@localhost:2552/user/service")
    val client = clientSystem.actorOf(Props(new ClientActor), "client")

    client ! Go(remoteService)
  }

  args match {
    case Array("--client") => startClient
    case Array("--server") => startService
    case _ => startService; startClient
  }

}