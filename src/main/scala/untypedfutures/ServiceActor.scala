package untypedfutures

import akka.actor.Actor

class ServiceActor extends Actor {

  def process(x: Int) = x*x

  def receive = {
    case x: Int =>
      log("Service enter")
      context.currentMessage.sender ! process(x)
      log("Service leave")
    case y =>
      log("Service unkown message " + y)
  }

}