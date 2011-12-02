package untypedfutures

import akka.actor.Actor

class ServiceActor extends Actor {

  def process(x: Int) = x*x

  def receive = {
    case x: Int =>
      log("Service enter")
      self.channel ! process(x)
      log("Service leave")
    case y =>
      log("Service unkown message " + y)
  }

}