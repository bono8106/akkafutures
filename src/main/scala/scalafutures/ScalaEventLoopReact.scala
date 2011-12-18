package scalafutures

import scala.actors.Actor._

object ScalaEventLoopReact extends App {

  val testActor = actor {
    lazy val handler: PartialFunction[Any, Unit] = {
      case message: String =>
        println("Message = " + message)
        self ! 42
        self mkBody {
          react {
            case answer: Int =>
              println("the answer to the universe and everything is " + answer)
          }
        } andThen eventloop(handler)
    }
    eventloop(handler)
  }

  testActor ! "Hello"
  testActor ! "World"

}