package object untypedfutures {

  import akka.actor.Actor
  import akka.actor.ActorSystem
  import akka.actor.Props

  lazy val actorSystem = ActorSystem("testuntyped")

  implicit val actorTimeout = actorSystem.settings.ActorTimeout

  def newThreadDispatcher(name: String) = actorSystem.dispatcherFactory.
    newDispatcher(name).setCorePoolSize(1).setMaxPoolSize(1).build

  def log(msg: String) {
    println("[" + Thread.currentThread.getName + "] " + msg)
  }

  def namedThreadActorOf(f: => Actor, name: String) =
    actorSystem.actorOf(Props(f).withDispatcher(newThreadDispatcher(name)))

}