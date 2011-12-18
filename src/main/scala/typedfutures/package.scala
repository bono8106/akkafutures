package object typedfutures {

  import akka.actor.TypedActor
  import akka.actor.ActorSystem
  import akka.actor.Props

  lazy val actorSystem = ActorSystem("testtyped")
  lazy val typedActors = TypedActor(actorSystem)

  def newThreadDispatcher(name: String) = actorSystem.dispatcherFactory.
    newDispatcher(name).setCorePoolSize(1).setMaxPoolSize(1).build

  def log(msg: String) {
    println("[" + Thread.currentThread.getName + "] " + msg)
  }

  def namedThreadActorOf[R <: AnyRef, T <: R](interface: Class[R], impl: Class[T], name: String): R =
    typedActors.typedActorOf(interface, impl, Props().withDispatcher(newThreadDispatcher(name)))

}