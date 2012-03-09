package untypedfutures

import akka.actor.Actor
import akka.pattern.ask

object TwoFoldForward extends App {

  case object Go

  class A extends Actor {

    val b = namedThreadActorOf(new B, "B")

    def receive = {
      case Go =>
        val f = b ? "hello"
        f onSuccess { case r => log("resolved to " + r + " == " + f.value.get.right.get) }
        f onFailure { case e => log("smashed with " + e) }
    }
  }

  class B extends Actor {

    val c = namedThreadActorOf(new C, "C")
    val d = namedThreadActorOf(new D, "D")

    def receive = {
      case m: String =>
        c forward m
        d forward m
    }

  }

  class C extends Actor {
    def receive = {
      case m: String =>
        Thread.sleep(200)
        sender ! m + " world"
    }
  }

  class D extends Actor {
    def receive = {
      case m: String =>
        Thread.sleep(100)
        throw new Exception("error")
    }
  }

  val a = namedThreadActorOf(new A, "A")
  a ! Go

}