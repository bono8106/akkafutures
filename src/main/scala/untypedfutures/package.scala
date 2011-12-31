package object untypedfutures {

  import akka.actor.Actor
  import akka.actor.ActorSystem
  import akka.actor.Props
  import com.typesafe.config.ConfigFactory

  lazy val actorSystem = ActorSystem("testuntyped", ConfigFactory.parseString("""
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

  implicit lazy val actorTimeout = actorSystem.settings.ActorTimeout

  def log(msg: String) {
    println("[" + Thread.currentThread.getName + "] " + msg)
  }

  def namedThreadActorOf(f: => Actor, name: String) =
    actorSystem.actorOf(Props(f).withDispatcher("dispatchers." + name), name = name)

}