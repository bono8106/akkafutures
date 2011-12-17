package untypedfutures.letitcrash

import akka.actor.Actor
import untypedfutures.{namedThreadActorOf, log}

object LetItCrashTest extends App {

  case class Request(i: Int)
  case class Response(j: Int)
  case object Fail

  class Executive extends Actor {
    def receive = {
      case Request(i) =>
        log("processing " + i)
        Thread.sleep(100) // Do some work ..
        log("processed " + i)
        sender ! Response(i*i)
    }
  }

  class Director extends Actor {

    val exec = namedThreadActorOf(new Executive, "executive")

    /**
     * Counts requests submitted to the exec and pending completion.
     */
    var counter = 0

    def receive = {
      case request @ Request(i) =>
        exec ! request
        counter += 1
        log("processing " + i + "; pending request counter = " + counter)
      case Response(j) =>
        counter -= 1
        log("processed " + j + "; pending request counter = " + counter)
      case Fail =>
        Thread.sleep(1000) // let my mailbox accumulate some messages
        throw new RuntimeException("Director actor failing.")
    }

  }

  val director = namedThreadActorOf(new Director, "director")

  director ! Request(1)
  director ! Fail
  director ! Request(2)

}