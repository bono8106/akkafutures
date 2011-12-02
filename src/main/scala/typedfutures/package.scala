package object typedfutures {
  
  import akka.actor.TypedActor
  import akka.actor.TypedActorConfiguration
  import akka.dispatch.Dispatchers

  def newThreadDispatcher(name: String) = Dispatchers.
    newExecutorBasedEventDrivenDispatcher(name).setCorePoolSize(1).setMaxPoolSize(1).build
    
  def namedThreadActorOf[R <: AnyRef, T <: R](interface: Class[R], impl: Class[T], name: String): R = 
    TypedActor.newInstance(interface, impl, TypedActorConfiguration().dispatcher(newThreadDispatcher(name)))

  def log(msg: String) {
    println("[" + Thread.currentThread.getName + "] " + msg)
  }
  
}