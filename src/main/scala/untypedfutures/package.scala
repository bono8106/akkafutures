package object untypedfutures {
  
  import akka.dispatch.Dispatchers

  def newThreadDispatcher(name: String) = Dispatchers.
    newExecutorBasedEventDrivenDispatcher(name).setCorePoolSize(1).setMaxPoolSize(1).build

  def log(msg: String) {
    println("[" + Thread.currentThread.getName + "] " + msg)
  }

}