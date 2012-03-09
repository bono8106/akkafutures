package eorder

import akka.actor.{ ActorSystem, Props }
import com.typesafe.config.ConfigFactory

object RemoteEOrder extends App {
  
  // TODO Fails

  val defaultConfig = ConfigFactory.parseString("""
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
        port = 2552 #OVERRIDE

        message-frame-size = 10 MiB
      }
  """)

  def createSystem(name: String, port: Int) = {
    import collection.JavaConversions._

    val config = ConfigFactory.parseMap(Map(
        //"akka.cluster.nodename" -> name,
        "akka.remote.netty.port" -> port))

    ActorSystem(name, ConfigFactory.load(config.withFallback(defaultConfig)))
  }

  val aliceSystem = createSystem("aliceSystem", 2552)
  val bobSystem = createSystem("bobSystem", 2553)
  val carolSystem = createSystem("carolSystem", 2554)

  val alice = aliceSystem.actorOf(Props(new Alice), "Alice")
  val bob = bobSystem.actorOf(Props(new Bob), "Bob")
  val carol = carolSystem.actorOf(Props(new Carol), "Carol")

  val bobRemote = aliceSystem.actorFor(bob.path)
  val carolRemote = aliceSystem.actorFor(carol.path)

  Actors.run(alice, bobRemote, carolRemote)

}