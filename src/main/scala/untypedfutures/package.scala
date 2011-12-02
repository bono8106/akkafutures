package object untypedfutures {
  
  import akka.actor.Actor
  import akka.dispatch.Dispatchers

  def newThreadDispatcher(name: String) = Dispatchers.
    newExecutorBasedEventDrivenDispatcher(name).setCorePoolSize(1).setMaxPoolSize(1).build
    
  def log(msg: String) {
    println("[" + Thread.currentThread.getName + "] " + msg)
  }

  def namedThreadActorOf(f: => Actor, name: String) = {
    val actor = Actor.actorOf(f)
    actor.dispatcher = newThreadDispatcher(name)
    actor.start
  }

}