package untypedfutures

import akka.actor.Actor

class ServiceActor extends Actor {

  self.dispatcher = newThreadDispatcher("service")

  def process(x: Int) = x*x

  def receive = {
    case x: Int =>
      log("In service actor")
      self.channel ! process(x)
      log("Out of service actor")
    case y =>
      log("Unkown message " + y)
  }

}