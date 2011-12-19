package eorder

import akka.actor.Actor
import akka.actor.ActorRef
import scala.annotation.tailrec

object Actors {
  def run(alice: ActorRef, bob: ActorRef, carol: ActorRef) {
    alice ! (bob, carol)
  }
}

class Alice extends Actor {

  /**
   * WOW! tailrec works practically the same and as fast
   * (in parallel!) as the function that does parallel fold below.
   */
  @tailrec
  private def makeLargeMessage(size: Int = 1000000, list: List[Int] = List.empty[Int]): List[Int] = size match {
    case 0 => list
    case _ => makeLargeMessage(size - 1, size.toString.hashCode :: list)
  }

  private def makeLargeMessagePar(size: Int = 1000000) = {
    (1 to size).par.foldRight(List.empty[Int])(_.toString.hashCode :: _)
  }

  def receive = {
    case (bob: ActorRef, carol: ActorRef) =>
      // Send Carol a large message slowly
      carol ! makeLargeMessage()

      // Introduce Carol to Bob
      bob ! carol
  }

}

class Bob extends Actor {

  def receive = {
    case carol: ActorRef =>
      // Say hello to Carol
      carol ! List("Hello from Bob")
  }

}

class Carol extends Actor {

  def receive = {
    case m: List[_] =>
      println("Carol got a message from " + sender + " of length " + m.size)
  }

}