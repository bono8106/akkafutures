package eorder

import akka.actor._

object LocalEOrder extends App {

  val actorSystem = ActorSystem("localSystem")

  val alice = actorSystem.actorOf(Props(new Alice), "Alice")
  val bob = actorSystem.actorOf(Props(new Bob), "Bob")
  val carol = actorSystem.actorOf(Props(new Carol), "Carol")

  Actors.run(alice, bob, carol)

}