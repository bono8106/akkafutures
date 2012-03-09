package object untypedfutures {

  import akka.actor.Actor
  import akka.actor.ActorRefFactory
  import akka.actor.ActorSystem
  import akka.actor.Props
  import akka.util.Timeout
  import com.typesafe.config.ConfigFactory

  implicit lazy val actorSystem = ActorSystem("testuntyped", ConfigFactory.parseString("""
      dispatchers {
        client.type = "PinnedDispatcher"
        service.type = "PinnedDispatcher"
        testactor.type = "PinnedDispatcher"
        sender1.type = "PinnedDispatcher"
        sender2.type = "PinnedDispatcher"
        A.type = "PinnedDispatcher"
        B.type = "PinnedDispatcher"
        C.type = "PinnedDispatcher"
        D.type = "PinnedDispatcher"
      }
      """))

  implicit lazy val actorTimeout = Timeout(3000L)

  def log(msg: String) {
    println("[" + Thread.currentThread.getName + "] " + msg)
  }

  def namedThreadActorOf(f: => Actor, name: String)(implicit actorRefFactory: ActorRefFactory) =
    actorRefFactory.actorOf(Props(f).withDispatcher("dispatchers." + name), name = name)

}