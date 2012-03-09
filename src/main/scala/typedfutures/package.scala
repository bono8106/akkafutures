package object typedfutures {

  import akka.actor.TypedActor
  import akka.actor.ActorSystem
  import akka.actor.TypedProps
  import com.typesafe.config.ConfigFactory

  lazy val actorSystem = ActorSystem("testtyped", ConfigFactory.parseString("""
      dispatchers {
        client.type = "PinnedDispatcher"
        service.type = "PinnedDispatcher"
      }
      """))

  lazy val typedActors = TypedActor(actorSystem)

  def log(msg: String) {
    println("[" + Thread.currentThread.getName + "] " + msg)
  }

  def namedThreadActorOf[R <: AnyRef, T <: R](interface: Class[R], impl: Class[T], name: String): R =
    typedActors.typedActorOf(TypedProps[T](interface, impl).withDispatcher("dispatchers." + name))

}