package untypedfutures

import akka.actor.Actor

object MessageSenderTest extends App {

  class SenderActor extends Actor {

    def receive = {
      case m: Int =>
      log("Service Sender A1 = " + self.channel + "; " + self.sender)
      Thread.sleep(100)
      log("Service Sender A2 = " + self.channel + "; " + self.sender)
    }

  }

  val actor = namedThreadActorOf(new SenderActor, "service")

  for (i <- 1 to 2) {
    namedThreadActorOf(new Actor {
      def receive = { case None => actor ! i }
    }, "sender-" + i) ! None
    Thread.sleep(40)
  }

}